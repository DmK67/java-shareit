package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.StatusState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.*;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.utility.ValidationClass.*;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingDto addBooking(BookingDto bookingDto, Long bookerId) { // Метод добавления бронирования
        // Получение и проверка на наличии вещи в БД
        Item itemDB = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Вещь по id=" + bookingDto.getItemId() + " не существует!"));
        if (!itemDB.getAvailable()) { // Проверка доступности к бронированию
            throw new ValidateException("Вещь нельзя забронировать, поскольку available = false.");
        }
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + bookerId + " не существует!"));
        checkBookingDtoWhenAdd(bookingDto); // Проверка полей объекта BookingDto перед добавлением
        checkBookerIsTheOwner(itemDB, bookerId); // Проверка является ли арендодатель-владельцем вещи
        bookingDto.setStatus(Status.WAITING);
        Booking booking = toBooking(bookingDto);
        booking.setBooker(booker);
        booking.setItem(itemDB);
        return toBookingDto(bookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование по id=" + id + " не существует!"));
        return toBookingDto(booking);
    }

    @Transactional
    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getListBookingsUserById(Long userId, String state, Integer from, Integer size) {
        // Метод получения списка всех бронированний пользователя по id
        userRepository.findById(userId) // Проверяем существование пользователя в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + userId + " не существует!"));
        checkStatusState(state); // Проверка statusState
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
        return toListBookingDto(newListResult);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto getBookingByIdAndStatus(Long ownerId, Long bookingId) {
        // Метод получение данных о конкретном бронировании (включая его статус)
        userRepository.findById(ownerId) // Проверяем существование пользователя в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + ownerId + " не существует!"));
        BookingDto bookingById = getBookingById(bookingId); // Проверяем существование бронирования в БД
        checkBookerOrOwner(ownerId, bookingId); // Проверяем владельца вещи
        // и клиента бронирования на соответствие
        return bookingById;
    }

    @Transactional
    @Override
    public BookingDto updateBooking(Long ownerId, Boolean approved, Long bookingId) { // Метод обновления бронирования
        userRepository.findById(ownerId) // Проверяем существование пользователя в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + ownerId + " не существует!"));
        BookingDto bookingDtoFromBD = getBookingById(bookingId); // Получаем и проверяем существование бронирования в БД
        Booking bookingFromBD = toBooking(bookingDtoFromBD);
        bookingFromBD.setBooker(toUser(bookingDtoFromBD.getBooker()));
        checkOwnerItemAndBooker(bookingFromBD.getItem().getId(), ownerId, bookingId);
        // Проверяем соответствие владельца вещи
        if (!bookingFromBD.getStatus().equals(Status.WAITING)) {
            throw new ValidateException("Статус изменить не возможно.");
        }
        if (approved) {
            bookingFromBD.setStatus(Status.APPROVED);
            return toBookingDto(bookingRepository.save(bookingFromBD));
        } else {
            bookingFromBD.setStatus(Status.REJECTED);
            return toBookingDto(bookingRepository.save(bookingFromBD));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getListBookingsOwnerById(Long owner, String state, Integer from, Integer size) {
        userRepository.findById(owner) // Проверяем существование пользователя в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + owner + " не существует!"));
        checkStatusState(state); // Проверка statusState
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
        return toListBookingDto(newListResult);
    }

    private void checkBookerOrOwner(Long userId, Long bookingId) {
        // Метод проверки владельца вещи и клиента бронирования перед просмотром
        Booking booking = bookingRepository.findById(bookingId).get();
        Item item = booking.getItem();
        if (item.getOwner().getId().equals(userId)) {
            return;
        } else if (booking.getBooker().getId().equals(userId)) {
            return;
        }
        log.error("Просмотр запрещен! Пользователь по Id: {} не является ни владельцем вещи ни клиентом " +
                "бронирования!", userId);
        throw new NotFoundException("Просматривать информацию о бронированнии вещи может только владелец " +
                "или клиент бронирования!");
    }

}
