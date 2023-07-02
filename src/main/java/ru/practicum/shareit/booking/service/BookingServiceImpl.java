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
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final UserService userService;

    private final ValidationService validationService;

    @Override
    public Booking addBooking(BookingDto bookingDto, Long bookerId) { // Метод добавления бронирования
        Item itemDB = itemService.getItemById(bookingDto.getItemId()); // Получение и проверка на наличии вещи в БД
        itemService.checkingIsAvailable(itemDB); // Проверка доступности к бронированию
        userService.getUserById(bookerId); // Получение и проверка на наличии пользователя в БД
        //validationService.checkBookingDtoWhenAdd(bookingDto); // Проверка полей объекта BookingDto перед добавлением
        validationService.checkBookerIsTheOwner(itemDB, bookerId); // Проверка является ли арендодатель - владельцем вещи
        bookingDto.setStatus(Status.WAITING);
        bookingDto.setStatusState(StatusState.ALL);
        Booking booking = toBooking(bookingDto);
        booking.setBooker(userService.getUserById(bookerId));
        booking.setItem(itemService.getItemById(bookingDto.getItemId()));
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование по id=" + id + " не существует!"));
        return booking;
    }

    @Override
    public Booking updateBooking(Booking booking, Long userId) {
        return null;
    }

    @Override
    public void deleteBooking(Long id) {
    }

    @Override
    public List<Booking> getListBookingsUserById(Long userId, String state) { // Метод получения списка всех бронированний пользователя по id
        userService.getUserById(userId); // Проверяем существование пользователя в БД
        validationService.checkStatusState(state); // Проверка statusState
        List<Booking> reverseList = new ArrayList<>(bookingRepository.findAll());
        Collections.reverse(reverseList);
        return reverseList;
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
    public Booking updateStatusBooking(Long ownerId, Boolean approved, Long bookingId) {
        // Метод подтверждения или отклонения запроса на бронирование
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
}
