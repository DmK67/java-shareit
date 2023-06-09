package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    Map<Long, User> userMap = new HashMap<>();
    private Long count = 0L;

    @Override
    public User addUser(User user) {
        user.setId(++count);
        userMap.put(user.getId(), user);
        return userMap.get(user.getId());
    }

    @Override
    public User getUserById(long id) {
        if (userMap.containsKey(id)) {
            return userMap.get(id);
        } else {
            throw new NotFoundException("Пользователь по id=" + id + " не существует");
        }
    }

    @Override
    public Map<Long, User> getUserMap() {
        return userMap;
    }

    @Override
    public User updateUser(User user) {
        User updateUser = userMap.get(user.getId());
        if (user.getName() == null) {
            user.setName(updateUser.getName());
        }
        if (!user.getName().isBlank() && !updateUser.getName().equals(user.getName())) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(updateUser.getEmail());
        }
        if (!user.getEmail().isBlank() && !updateUser.getEmail().equals(user.getEmail())) {
            updateUser.setEmail(user.getEmail());
        }
        userMap.remove(user.getId());
        userMap.put(updateUser.getId(), updateUser);
        return userMap.get(updateUser.getId());
    }

    @Override
    public void deleteUser(Long id) {
        userMap.remove(id);
    }

    @Override
    public List<User> getListUsers() {
        return new ArrayList<>(userMap.values());
    }

}
