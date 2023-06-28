package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
//@AllArgsConstructor
//@NoArgsConstructor
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping // Метод добавления бронирования
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        log.info("Добавляем бронирования пользователем по id={}.", bookerId);
    return toBookingDto(bookingService.addBooking(bookingDto, bookerId));
    }

    /*
    @PatchMapping("/{userId}") // Метод обновления пользователя по его id
    public UserDto updateUser(@Valid @Min(1) @PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        log.info("Обновляем пользователя по Id={}.", userId);
        return toUserDto(userService.updateUser(toUser(userDto), userId));
    }

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
