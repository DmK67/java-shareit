package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserRepositoryImpl {
    /*
    private Map<Long, User> userMap = new HashMap<>();
    private Long count = 0L;

    @Override
    public User addUser(User user) { // Метод добавления пользователя
        user.setId(++count);
        userMap.put(user.getId(), user);
        log.info("Пользователь {} успешно добавлен!", userMap.get(user.getId()));
        return userMap.get(user.getId());
    }

    @Override
    public User getUserById(long id) { // Метод получения пользователя по id
        if (userMap.containsKey(id)) {
            log.info("Вывод пользователя по id={}.", id);
            return userMap.get(id);
        } else {
            log.error("Ошибка! Пользователь по id={} отсутствует в памяти.", id);
            throw new NotFoundException("Пользователь по id=" + id + " не существует!");
        }
    }

    @Override
    public User updateUser(User user) { // Метод обновления пользователя
        userMap.put(user.getId(), user);
        log.info("Пользователь по id={} успешно обновлен!", user.getId());
        return userMap.get(user.getId());
    }

    @Override
    public void deleteUser(Long id) { // Метод удаления пользователя по id
        userMap.remove(id);
        log.info("Пользователь по id={} успешно удален!", id);
    }

    @Override
    public List<User> getListUsers() { // Метод получения списка всех пользователей
        log.info("Выводим список всех пользователей.");
        return new ArrayList<>(userMap.values());
    }
*/
}
