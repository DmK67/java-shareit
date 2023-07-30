package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static ru.practicum.shareit.utility.ValidationClass.checkUniqueEmailUserAdd;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Transactional
    @Override
    public User addUser(User user) {
        checkUniqueEmailUserAdd(user); // Проверка поля email объекта user на пустые строки и пробелы
        return repository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(long id) { // Метод получения пользователя по id
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + id + " не существует!"));
        return user;
    }

    @Transactional
    @Override
    public User updateUser(User user, Long userId) {
        User updateUser = getUserById(userId); // Проверка пользователя по его id на существование в БД
        user.setId(userId);
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
        return repository.save(updateUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getListUsers() {
        return repository.findAll();
    }

}
