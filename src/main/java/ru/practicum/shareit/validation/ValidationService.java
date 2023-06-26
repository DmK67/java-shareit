package ru.practicum.shareit.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.concurrent.atomic.AtomicMarkableReference;

@Service
@AllArgsConstructor
@Slf4j
public class ValidationService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final EntityManager entityManager;

    public void checkUniqueEmailUserAdd(User user) { // Метод проверки поля e-mail на пустые строки и пробелы при добавлении
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Ошибка! Пользователь с пустым e-mail не может быть добавлен!");
            throw new ValidateException("Ошибка! Пользователь с пустым e-mail не может быть добавлен!");
        }
    }

    public void checkUniqueEmailUserUpdate(User user) { // Метод проверки уникальности e-mail при обновлении
        for (User u : userRepository.findAll()) {
            if (u.getEmail().equals(user.getEmail()) && !u.getId().equals(user.getId())) {
                log.error("Ошибка! Пользователь с e-mail: {} уже существует!", user.getEmail());
                throw new ConflictException("Ошибка! Пользователь с e-mail " + user.getEmail() + " уже существует!");
            }
        }
    }

    public void checkOwnerItem(Long itemId, Long ownerId) { // Метод проверки соответствия владельца вещи
        if (!itemRepository.getItemMap().get(itemId).getOwner().equals(ownerId)) {
            log.error("Ошибка! Пользователь по Id: {} не является владельцем вещи! " +
                    "Изменение вещи ЗАПРЕЩЕНО!", ownerId);
            throw new ForbiddenException("Вносить изменения в параметры вещи может только владелец!");
        }
    }

    public void checkItemDtoWhenAdd(ItemDto itemDto) { // Метод проверки полей объекта itemDto перед добавлением
        if (itemDto.getAvailable() == null
                || itemDto.getName() == null || itemDto.getName().isBlank()
                || itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            log.error("Ошибка! Вещь с пустыми полями не может быть добавлена!");
            throw new ValidateException("Ошибка! Вещь с пустыми полями не может быть добавлена!");
        }
    }

    public boolean existsIdUser(Long key) { // Метод проверки существования id в БД
        if (entityManager.getReference(User.class, key) != null) {
            return true;
        } else {
            log.error("Ошибка! Пользователь по id={} отсутствует в БД.", key);
            throw new NotFoundException("Пользователь по id=" + key + " не существует!");
        }
    }

}