package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.ValidationService;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    private final ValidationService validationService;

    @Override
    public User addUser(User user) {
        validationService.checkUniqueEmailUserAdd(user); // Проверка объекта user на уникальность e-mail
        //return repository.addUser(user);
        return repository.save(user);
    }

    @Override
    public User getUserById(long id) {
        //return repository.getUserById(id);
        return repository.findById(id).get();
    }

    @Override
    public User updateUser(User user, Long userId) {
        getUserById(userId); // Проверка пользователя по его id на существование в памяти
        user.setId(userId);
        validationService.checkUniqueEmailUserUpdate(user); // Проверка объекта userDto на уникальность e-mail
        User updateUser = repository.getById(user.getId());
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
        //return repository.updateUser(updateUser);
        return repository.save(updateUser);
    }

    @Override
    public void deleteUser(Long id) {
        //repository.deleteUser(id);
        repository.deleteById(id);
    }

    @Override
    public List<User> getListUsers() {
        //return repository.getListUsers();
        return repository.findAll();
    }

}
