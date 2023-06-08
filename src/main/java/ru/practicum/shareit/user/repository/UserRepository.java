package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;

public interface UserRepository {

    User addUser(User user);
    User getUserById(long id);

    Map<Long, User> getUserMap();

    //User searchUserByEmail(String email);

    User updateUser(User user);

    void deleteUser(Long id);

    List<User> getListUsers();

}

