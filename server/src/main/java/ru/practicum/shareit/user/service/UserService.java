package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    User getUserById(long id);

    User updateUser(User user, Long userId);

    void deleteUser(Long id);

    List<UserDto> getListUsers();

}
