package ru.practicum.shareit.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class ValidationService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

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
        Item item = itemRepository.findById(itemId).get();
        if (!item.getOwner().getId().equals(ownerId)) {
            log.error("Ошибка! Пользователь по Id: {} не является владельцем вещи! " +
                    "Изменение вещи ЗАПРЕЩЕНО!", ownerId);
            throw new ForbiddenException("Вносить изменения в параметры вещи может только владелец!");
        }
    }

    public void checkItemDtoWhenAdd(ItemDto itemDto) { // Метод проверки полей объекта ItemDto перед добавлением
        if (itemDto.getAvailable() == null
                || itemDto.getName() == null || itemDto.getName().isBlank()
                || itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            log.error("Ошибка! Вещь с пустыми полями не может быть добавлена!");
            throw new ValidateException("Ошибка! Вещь с пустыми полями не может быть добавлена!");
        }
    }

    public void checkBookingDtoWhenAdd(BookingDto bookingDto) { // Метод проверки полей объекта BookingDto перед добавлением
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            log.error("Ошибка! Поля начала и окончания бронирования не могут быть пустыми!");
            throw new ValidateException("Ошибка! Поля начала и окончания бронирования не могут быть пустыми!");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            log.error("Ошибка! Указано неправильно дата или время бронирования!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
        }
        if (bookingDto.getEnd().equals(bookingDto.getStart())) {
            log.error("Ошибка! Указано неправильно дата/время бронирования!");
            throw new ValidateException("Ошибка! Дата и время начала бронирования должно отличатся " +
                    "от даты и времени окончания бронирования!");
        }
        if (bookingDto.getStart().isAfter(LocalDateTime.now()) || bookingDto.getEnd().isAfter(LocalDateTime.now())) {
            log.error("Ошибка! Указано неправильно дата/время бронирования!");
            throw new ValidateException("Ошибка! Дата и время начала бронирования или окончания бронирования должно " +
                    "быть позже точного времени!");
        }
    }

}