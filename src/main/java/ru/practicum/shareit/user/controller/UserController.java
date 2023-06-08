package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import java.util.List;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final ItemService itemService;
    private final ValidationService validationService;

    //Ранее вы уже создавали контроллеры для управления пользователями — создания, редактирования и просмотра.
    //Здесь вам нужно сделать то же самое. Создайте класс UserController и методы в нём для основных CRUD-операций.
    //Также реализуйте сохранение данных о пользователях в памяти.
    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        validationService.checkUniqueEmailUserAdd(toUser(userDto));
        log.info("Добавляем пользователя по имени: " + userDto.getName());
        return toUserDto(userService.addUser(toUser(userDto)));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @Min(1) @PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        userService.getUserById(userId);
        userDto.setId(userId);
        validationService.checkUniqueEmailUserUpdate(toUser(userDto));
        log.info("Обновляем пользователя по Id=" + userId);
        return toUserDto(userService.updateUser(toUser(userDto)));
    }

    @GetMapping("/{userId}")
    public UserDto updateUser(@Valid @Min(1) @PathVariable Long userId) {
        log.info("Получаем пользователя по Id=" + userId);
        return toUserDto(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@Valid @Min(1) @PathVariable Long userId) {
        log.info("Удалаяем пользователя по Id=" + userId);
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<User> getListUsers() {
        log.info("Получаем список всех пользователей");
        return userService.getListUsers();
    }
}
