package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User addUser(User user);

    User getUserById(long id);

    User updateUser(User user);

    void deleteUser(Long id);

    List<User> getListUsers();

}

