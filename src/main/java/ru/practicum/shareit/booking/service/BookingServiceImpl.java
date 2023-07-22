package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.StatusState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.listResultAddItemAndAddBooker;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Booking addBooking(BookingDto bookingDto, Long bookerId) { // Метод добавления бронирования
        Item itemDB = itemService.getItemById(bookingDto.getItemId()); // Получение и проверка на наличии вещи в БД
        itemService.checkingIsAvailable(itemDB); // Проверка доступности к бронированию
        User booker = userService.getUserById(bookerId); // Получение и проверка на наличии пользователя в БД
        validationService.checkBookingDtoWhenAdd(bookingDto); // Проверка полей объекта BookingDto перед добавлением
        validationService.checkBookerIsTheOwner(itemDB, bookerId); // Проверка является ли арендодатель-владельцем вещи
        bookingDto.setStatus(Status.WAITING);
        Booking booking = toBooking(bookingDto);
        booking.setBooker(booker);
        booking.setItem(Item.builder()
                .id(itemDB.getId())
                .name(itemDB.getName())
                .build());
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование по id=" + id + " не существует!"));
        return booking;
    }

    @Transactional
    @Override
    public void deleteBooking(Long id) {
    }

    @Transactional
    @Override
    public List<Booking> getListBookingsUserById(Long userId, String state, Integer from, Integer size) {
        // Метод получения списка всех бронированний пользователя по id
        userService.getUserById(userId); // Проверяем существование пользователя в БД
        validationService.checkStatusState(state); // Проверка statusState
        Pageable page = PageRequest.of(from / size, size);
        StatusState statusState = StatusState.valueOf(state);
        List<Booking> listResult = new ArrayList<>();
        switch (statusState) {
            case CURRENT:
                listResult = bookingRepository.findAllByBookerIdAndStateCurrent(userId, page);
                break;
            case PAST:
                listResult = bookingRepository.findAllByBookerIdAndStatePast(userId, Status.APPROVED, page);
                break;
            case FUTURE:
                listResult = bookingRepository.findAllByBookerIdAndStateFuture(userId, page);
                break;
            case WAITING:
                listResult = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page);
                break;
            case REJECTED:
                listResult = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page);
                break;
            case ALL:
                listResult = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page);
                break;
        }
        List<Booking> newListResult = listResultAddItemAndAddBooker(listResult);
        return newListResult;
    }

    @Transactional
    @Override
    public Booking getBookingByIdAndStatus(Long ownerId, Long bookingId) {
        // Метод получение данных о конкретном бронировании (включая его статус)
        userService.getUserById(ownerId); // Проверяем существование пользователя в БД
        Booking bookingById = getBookingById(bookingId); // Проверяем существование бронирования в БД
        validationService.checkBookerOrOwner(ownerId, bookingId); // Проверяем владельца вещи
        // и клиента бронирования на соответствие
        return bookingById;
    }

    @Transactional
    @Override
    public Booking updateBooking(Long ownerId, Boolean approved, Long bookingId) { // Метод обновления бронирования
        userService.getUserById(ownerId); // Проверяем существование пользователя в БД
        Booking bookingFromBD = getBookingById(bookingId); // Получаем и проверяем существование бронирования в БД
        validationService.checkOwnerItemAndBooker(bookingFromBD.getItem().getId(), ownerId, bookingId);
        // Проверяем соответствие владельца вещи
        if (!bookingFromBD.getStatus().equals(Status.WAITING)) {
            throw new ValidateException("Статус изменить не возможно.");
        }
        if (approved) {
            bookingFromBD.setStatus(Status.APPROVED);
            bookingRepository.save(bookingFromBD);
        } else {
            bookingFromBD.setStatus(Status.REJECTED);
            bookingRepository.save(bookingFromBD);
        }
        return bookingRepository.save(bookingFromBD);
    }

    @Transactional
    @Override
    public List<Booking> getListBookingsOwnerById(Long owner, String state, Integer from, Integer size) {
        userService.getUserById(owner); // Проверяем существование пользователя в БД
        validationService.checkStatusState(state); // Проверка statusState
        Pageable page = PageRequest.of(from / size, size);
        StatusState statusState = StatusState.valueOf(state);
        List<Booking> listResult = new ArrayList<>();
        switch (statusState) {
            case ALL: {
                listResult = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(owner, page);
                break;
            }
            case CURRENT: {
                listResult = bookingRepository.findAllByItemOwnerAndStateCurrent(owner, page);
                break;
            }
            case PAST: {
                listResult = bookingRepository.findAllByItemOwnerIdAndStatePast(owner, Status.APPROVED, page);
                break;
            }
            case FUTURE: {
                listResult = bookingRepository.findAllByItemOwnerIdAndStateFuture(owner, Status.REJECTED, page);
                break;
            }
            case WAITING: {
                listResult = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(owner,
                        Status.WAITING, page);
                break;
            }
            case REJECTED: {
                listResult = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(owner,
                        Status.REJECTED, page);
                break;
            }
        }
        List<Booking> newListResult = listResultAddItemAndAddBooker(listResult);
        return newListResult;
    }

}
