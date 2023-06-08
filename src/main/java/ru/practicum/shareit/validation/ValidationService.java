package ru.practicum.shareit.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@AllArgsConstructor
@Slf4j
public class ValidationService {
    UserRepository repository;

    public void checkUniqueEmailUserAdd(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Ошибка! Пользователь с пустым e-mail не может быть добавлен!");
            throw new ValidateException("Ошибка! Пользователь с пустым e-mail не может быть добавлен!");
        }
        if (!repository.getUserMap().containsKey(user.getId())) {
            for (User u : repository.getUserMap().values()) {
                if (u.getEmail().equals(user.getEmail())) {
                    log.error("Ошибка! Пользователь с e-mail: " + user.getEmail() + " уже существует!");
                    throw new ConflictException("Ошибка! Пользователь с e-mail " + user.getEmail() + " уже существует!");
                }
            }
        } else {
            for (User u : repository.getUserMap().values()) {
                if (u.getEmail().equals(user.getEmail()) && u.getId() != user.getId()) {
                    log.error("Ошибка! Пользователь с e-mail: " + user.getEmail() + " уже существует!");
                    throw new ConflictException("Ошибка! Пользователь с e-mail " + user.getEmail() + " уже существует!");
                }
            }
        }
    }

    public void checkUniqueEmailUserUpdate(User user) {
        for (User u : repository.getUserMap().values()) {
            if (u.getEmail().equals(user.getEmail()) && u.getId() != user.getId()) {
                log.error("Ошибка! Пользователь с e-mail: " + user.getEmail() + " уже существует!");
                throw new ConflictException("Ошибка! Пользователь с e-mail " + user.getEmail() + " уже существует!");
            }
        }
    }

}