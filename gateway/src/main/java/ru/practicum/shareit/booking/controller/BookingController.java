package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;


    /**
     * Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем,
     * а затем подтверждён владельцем вещи. Эндпоинт — POST /bookings.
     * После создания запрос находится в статусе WAITING — «ожидает подтверждения».
     */
    @PostMapping // Эндпоинт добавления бронирования
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                             @Valid @RequestBody BookingDto bookingDto) {
        log.info("Добавляем бронирования пользователем по id={}.", bookerId);
        return bookingClient.addBooking(bookingDto, bookerId);
    }

    /**
     * Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
     * Затем статус бронирования становится либо APPROVED, либо REJECTED.
     * Эндпоинт — PATCH /bookings/{bookingId}?approved={approved},
     * параметр approved может принимать значения true или false.
     */
    @PatchMapping("/{bookingId}") // Эндпоинт обновления бронирования
    public ResponseEntity<Object> updateBooking(@Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id",
            required = false) Long ownerId,
                                                @RequestParam(value = "approved", required = false) Boolean approved,
                                                @PathVariable Long bookingId) {
        log.info("Выполняем подтверждение или отклонение запроса на бронирование владельцем вещи по Id={}.", ownerId);
        return bookingClient.updateBooking(ownerId, approved, bookingId);
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус).
     * Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование.
     * Эндпоинт — GET /bookings/{bookingId}.
     */
    @GetMapping("/{bookingId}") // Эндпоинт получение данных о конкретном бронировании (включая его статус)
    public ResponseEntity<Object> getBookingByIdAndStatus(@Min(1) @NotNull
                                                          @RequestHeader(value = "X-Sharer-User-Id", required = false)
                                                              Long ownerId,
                                                          @PathVariable(required = false) Long bookingId) {
        log.info("Выполняем получение данных о конкретном бронировании по Id={} (включая его статус).", bookingId);
        return bookingClient.getBookingByIdAndStatus(ownerId, bookingId);
    }

    /**
     * Получение списка всех бронирований текущего пользователя. Эндпоинт — GET /bookings?state={state}.
     * Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     * Также он может принимать значения CURRENT (англ. «текущие»), **PAST** (англ. «завершённые»),
     * FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»),
     * REJECTED (англ. «отклонённые»).
     * Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
     */
    @GetMapping // Эндпоинт получения списка всех бронирований пользователя по id
    public ResponseEntity<Object> getListBookingsUserById(
            @Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получаем список всех бронирований пользователя по id={}", userId);
        return bookingClient.getListBookingsUserById(userId, state, from, size);
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя. Эндпоинт — GET /bookings/owner?state={state}.
     * Этот запрос имеет смысл для владельца хотя бы одной вещи.
     * Работа параметра state аналогична его работе в предыдущем сценарии.
     */
    @GetMapping("/owner") // Эндпоинт получения списка всех бронирований пользователя по id
    public ResponseEntity<Object> getListBookingsOwnerById(
            @Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
            @NotNull @NotBlank @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получаем список всех бронирований пользователя по id={}", userId);
        return bookingClient.getListBookingsOwnerById(userId, state, from, size);
    }

}
