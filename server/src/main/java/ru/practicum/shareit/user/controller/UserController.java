package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    /**
     * Создайте класс UserController и методы в нём для основных CRUD-операций.
     * Также реализуйте сохранение данных о пользователях в памяти.
     */
    @PostMapping // Эндпоинт добавления пользователя
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("Добавляем пользователя по имени: {}.", userDto.getName());
        return toUserDto(userService.addUser(toUser(userDto)));
    }

    @PatchMapping("/{userId}") // Эндпоинт обновления пользователя по его id
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Обновляем пользователя по Id={}.", userId);
        return toUserDto(userService.updateUser(toUser(userDto), userId));
    }

    @GetMapping("/{userId}") // Эндпоинт получения пользователя по его id
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Получаем пользователя по Id={}.", userId);
        return toUserDto(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}") // Эндпоинт удаления пользователя по id
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удалаяем пользователя по Id={}.", userId);
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getListUsers() { // Эндпоинт получения списка всех пользователей
        log.info("Получаем список всех пользователей.");
        return userService.getListUsers();
    }
}
