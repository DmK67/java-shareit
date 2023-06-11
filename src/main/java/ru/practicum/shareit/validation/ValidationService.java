package ru.practicum.shareit.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@AllArgsConstructor
@Slf4j
public class ValidationService {
    UserRepository userRepository;
    ItemRepository itemRepository;

    public void checkUniqueEmailUserAdd(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Ошибка! Пользователь с пустым e-mail не может быть добавлен!");
            throw new ValidateException("Ошибка! Пользователь с пустым e-mail не может быть добавлен!");
        }
        if (!userRepository.getUserMap().containsKey(user.getId())) {
            for (User u : userRepository.getUserMap().values()) {
                if (u.getEmail().equals(user.getEmail())) {
                    log.error("Ошибка! Пользователь с e-mail: " + user.getEmail() + " уже существует!");
                    throw new ConflictException("Ошибка! Пользователь с e-mail " + user.getEmail() + " уже существует!");
                }
            }
        } else {
            for (User u : userRepository.getUserMap().values()) {
                if (u.getEmail().equals(user.getEmail()) && u.getId() != user.getId()) {
                    log.error("Ошибка! Пользователь с e-mail: " + user.getEmail() + " уже существует!");
                    throw new ConflictException("Ошибка! Пользователь с e-mail " + user.getEmail() + " уже существует!");
                }
            }
        }
    }

    public void checkUniqueEmailUserUpdate(User user) {
        for (User u : userRepository.getUserMap().values()) {
            if (u.getEmail().equals(user.getEmail()) && u.getId() != user.getId()) {
                log.error("Ошибка! Пользователь с e-mail: " + user.getEmail() + " уже существует!");
                throw new ConflictException("Ошибка! Пользователь с e-mail " + user.getEmail() + " уже существует!");
            }
        }
    }

    public void checkOwnerItem(Long itemId, Long ownerId) {
        if (itemRepository.getItemMap().get(itemId).getOwner() != ownerId) {
            log.error("Ошибка! Пользователь по Id: " + ownerId + " не является владельцем вещи! " +
                    "Изменение вещи ЗАПРЕЩЕНО!");
            throw new ForbiddenException("Вносить изменения в параметры вещи может только владелец!");
        }
    }

//    public void checkItemDtoAvailable(boolean available, Long id, ItemDto itemDto) {
//        if (!available == itemRepository.getItemById(id).isAvailable()) {
//            itemDto.setAvailable(itemRepository.getItemById(id).isAvailable());
//        }
//    }
}