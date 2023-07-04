package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.StatusState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationService;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final ValidationService validationService;

    @Override
    public Booking addBooking(BookingDto bookingDto, Long bookerId) { // Метод добавления бронирования
        Item itemDB = itemService.getItemById(bookingDto.getItemId()); // Получение и проверка на наличии вещи в БД
        itemService.checkingIsAvailable(itemDB); // Проверка доступности к бронированию
        User booker = userService.getUserById(bookerId); // Получение и проверка на наличии пользователя в БД
        validationService.checkBookingDtoWhenAdd(bookingDto); // Проверка полей объекта BookingDto перед добавлением
        validationService.checkBookerIsTheOwner(itemDB, bookerId); // Проверка является ли арендодатель - владельцем вещи
        bookingDto.setStatus(Status.WAITING);
        Booking booking = toBooking(bookingDto);
        booking.setBooker(booker);
        booking.setItem(Item.builder()
                .id(itemDB.getId())
                .name(itemDB.getName())
                .build());
        Booking b = bookingRepository.save(booking);
        return b;
    }

    @Override
    public Booking getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование по id=" + id + " не существует!"));
        return booking;
    }

    @Override
    public void deleteBooking(Long id) {
    }

    @Override
    public List<Booking> getListBookingsUserById(Long userId, String state) {
        // Метод получения списка всех бронированний пользователя по id
        userService.getUserById(userId); // Проверяем существование пользователя в БД
        validationService.checkStatusState(state); // Проверка statusState
//        List<Booking> listResult = new ArrayList<>(bookingRepository.findAll());
//        List<Booking> newListResult = listResultAddItemAndAddBooker(listResult);
//        Collections.reverse(newListResult);
        StatusState statusState = StatusState.valueOf(state);
        List<Booking> listResult = new ArrayList<>();
        switch (statusState) {
            case CURRENT:
                listResult = bookingRepository.findAllByBookerIdAndStateCurrent(userId);
                break;
            case PAST:
                listResult = bookingRepository.findAllByBookerIdAndStatePast(userId, Status.APPROVED);
                break;
            case FUTURE:
                listResult = bookingRepository.findAllByBookerIdAndStateFuture(userId);
                break;
            case WAITING:
                listResult = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                listResult = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
            case ALL:
                listResult = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
        }
        List<Booking> newListResult = listResultAddItemAndAddBooker(listResult);
        return newListResult;
    }

    @Override
    public Booking getBookingByIdAndStatus(Long ownerId, Long bookingId) {
        // Метод получение данных о конкретном бронировании (включая его статус)
        userService.getUserById(ownerId); // Проверяем существование пользователя в БД
        getBookingById(bookingId); // Проверяем существование бронирования в БД
        validationService.checkBookerOrOwner(ownerId, bookingId); // Проверяем владельца вещи и клиента бронирования на соответствие
        return getBookingById(bookingId);
    }

    @Override
    public Booking updateBooking(Long ownerId, Boolean approved, Long bookingId) { // Метод обновления бронирования
        userService.getUserById(ownerId); // Проверяем существование пользователя в БД
        Booking bookingFromBD = getBookingById(bookingId); // Получаем и проверяем существование бронирования в БД
        validationService.checkOwnerItemAndBooker(bookingFromBD.getItem().getId(), ownerId, bookingId); // Проверяем соответствие владельца вещи
        validationService.checkStatusBooking(approved, bookingId); // Проверяем статус бронирования на изменение
        if (approved) {
            bookingFromBD.setStatus(Status.APPROVED);
            bookingRepository.save(bookingFromBD);
        } else {
            bookingFromBD.setStatus(Status.REJECTED);
            bookingRepository.save(bookingFromBD);
        }
        return bookingFromBD;
    }

    @Override
    public List<Booking> getListBookingsOwnerById(Long owner, String state) {
        userService.getUserById(owner); // Проверяем существование пользователя в БД
        validationService.checkStatusState(state); // Проверка statusState
        StatusState statusState = StatusState.valueOf(state);
        List<Booking> listResult = new ArrayList<>();
        switch (statusState) {
            case ALL: {
                listResult = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(owner);
                break;
            }
            case CURRENT: {
                listResult = bookingRepository.findAllByItemOwnerAndStateCurrent(owner);
                break;
            }
            case PAST: {
                listResult = bookingRepository.findAllByItemOwnerIdAndStatePast(owner, Status.APPROVED);
                break;
            }
            case FUTURE: {
                listResult = bookingRepository.findAllByBookerIdAndStateFuture(owner);
                break;
            }
            case WAITING: {
                listResult = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(owner, Status.WAITING);
                break;
            }
            case REJECTED: {
                listResult = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(owner, Status.REJECTED);
                break;
            }
        }
        List<Booking> newListResult = listResultAddItemAndAddBooker(listResult);
        return newListResult;
    }

    protected List<Booking> listResultAddItemAndAddBooker(List<Booking> listResult) {
        for (Booking booking : listResult) {
            Item item = booking.getItem();
            booking.setItem(Item.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .build());
            User user = booking.getBooker();
            booking.setBooker(User.builder().id(user.getId()).build());
        }
        return listResult;
    }

}
