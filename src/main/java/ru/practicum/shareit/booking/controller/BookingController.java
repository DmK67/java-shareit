package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    /**
     * Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем,
     * а затем подтверждён владельцем вещи. Эндпоинт — POST /bookings.
     * После создания запрос находится в статусе WAITING — «ожидает подтверждения».
     */
    @PostMapping // Эндпоинт добавления бронирования
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        log.info("Добавляем бронирования пользователем по id={}.", bookerId);
        return toBookingDto(bookingService.addBooking(bookingDto, bookerId));
    }

    /**
     * Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
     * Затем статус бронирования становится либо APPROVED, либо REJECTED.
     * Эндпоинт — PATCH /bookings/{bookingId}?approved={approved},
     * параметр approved может принимать значения true или false.
     */
    @PatchMapping("/{bookingId}") // Эндпоинт подтверждения или отклонения запроса на бронирование
    public BookingDto updateStatusBooking(@Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                                          @RequestParam(value = "approved", required = false) Boolean approved,
                                          @PathVariable Long bookingId) {
        log.info("Выполняем подтверждение или отклонение запроса на бронирование владельцем вещи по Id={}.", ownerId);
        return toBookingDto(bookingService.updateStatusBooking(ownerId, approved, bookingId));
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус).
     * Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование.
     * Эндпоинт — GET /bookings/{bookingId}.
     */
    @GetMapping("/{bookingId}") // Эндпоинт получение данных о конкретном бронировании (включая его статус)
    public BookingDto getBookingByIdAndStatus(@Min(1) @NotNull
                                              @RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                                              @PathVariable(required = false) Long bookingId) {
        log.info("Выполняем получение данных о конкретном бронировании по Id={} (включая его статус).", bookingId);
        return toBookingDto(bookingService.getBookingByIdAndStatus(ownerId, bookingId));
    }

    @GetMapping // Эндпоинт получения списка всех бронирований пользователя по id
    public List<Booking> getListBookingsUserById(@Min(1) @NotNull
                                                 @RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        log.info("Получаем список всех бронирований пользователя по id={}", userId);
        return bookingService.getListBookingsUserById(userId);
    }

    @GetMapping("/owner") // Эндпоинт получения списка всех бронирований пользователя по id
    public List<Booking> getListBookingsOwnerById(@Min(1) @NotNull
                                                 @RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        log.info("Получаем список всех бронирований пользователя по id={}", userId);
        return bookingService.getListBookingsUserById(userId);
    }

    /*
    @GetMapping("/{userId}") // Метод получения пользователя по его id
    public UserDto updateUser(@Valid @Min(1) @PathVariable Long userId) {
        log.info("Получаем пользователя по Id={}.", userId);
        return toUserDto(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}") // Метод удаления пользователя по id
    public void deleteUser(@Valid @Min(1) @PathVariable Long userId) {
        log.info("Удалаяем пользователя по Id={}.", userId);
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<User> getListUsers() { // Метод получения списка всех пользователей
        log.info("Получаем список всех пользователей.");
        return userService.getListUsers();
    }

     */
}
