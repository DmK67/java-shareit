package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User addUser(User user) {
        return repository.addUser(user);
    }

    @Override
    public User getUserById(long id) {
        return repository.getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        return repository.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteUser(id);
    }

    @Override
    public List<User> getListUsers() {
        return repository.getListUsers();
    }
}
