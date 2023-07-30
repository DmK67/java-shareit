package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    /**
     * Создайте класс UserController и методы в нём для основных CRUD-операций.
     * Также реализуйте сохранение данных о пользователях в памяти.
     */
    @PostMapping // Эндпоинт добавления пользователя
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Добавляем пользователя по имени: {}.", userDto.getName());
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{userId}") // Эндпоинт обновления пользователя по его id
    public ResponseEntity<Object> updateUser(@Min(1) @PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Обновляем пользователя по Id={}.", userId);
        return userClient.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}") // Эндпоинт получения пользователя по его id
    public ResponseEntity<Object> getUserById(@Min(1) @PathVariable Long userId) {
        log.info("Получаем пользователя по Id={}.", userId);
        return userClient.getUserById(userId);
    }

    @DeleteMapping("/{userId}") // Эндпоинт удаления пользователя по id
    public void deleteUser(@Min(1) @PathVariable Long userId) {
        log.info("Удалаяем пользователя по Id={}.", userId);
        userClient.deleteUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getListUsers() { // Эндпоинт получения списка всех пользователей
        log.info("Получаем список всех пользователей.");
        return userClient.getListUsers();
    }
}
