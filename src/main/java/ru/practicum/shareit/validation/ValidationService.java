package ru.practicum.shareit.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ValidationService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

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

    public void checkOwnerItemAndBooker(Long itemId, Long ownerId, Long bookingId) { // Метод проверки соответствия владельца вещи
        Item item = itemRepository.findById(itemId).get();
        Booking booking = bookingRepository.findById(bookingId).get();
        if (item.getOwner().getId().equals(ownerId)) {
            return;
        }
        if (booking.getBooker().getId().equals(ownerId)) {
            log.error("Ошибка! Пользователь по Id: {} является арендатором вещи! " +
                    "Изменение статуса вещи ЗАПРЕЩЕНО!", ownerId);
            throw new NotFoundException("Вносить изменения в параметры вещи может только владелец!");
        }
        if (!item.getOwner().getId().equals(ownerId)) {
            log.error("Ошибка! Пользователь по Id: {} не является владельцем вещи! " +
                    "Изменение вещи ЗАПРЕЩЕНО!", ownerId);
            throw new ValidateException("Вносить изменения в параметры вещи может только владелец!");
        }
    }

    public void checkStatusBooking(Boolean approved, Long bookingId) { // Метод проверки статуса бронирования
        Booking booking = bookingRepository.findById(bookingId).get();
        if (booking.getStatus().name().equals("APPROVED") && approved) {
            log.error("Ошибка! Статус бронирования установлен APPROVED, повторно изменить статус на APPROVED не возможно!");
            throw new ValidateException("Статус APPROVED был установлен ранее.");
        }
        if (booking.getStatus().name().equals("REJECTED") && !approved) {
            log.error("Ошибка! Статус бронирования установлен REJECTED, повторно изменить статус на REJECTED не возможно!");
            throw new ValidateException("Статус REJECTED был установлен ранее.");
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

    public void checkBookerIsTheOwner(Item itemDB, Long bookerId) { // Метод проверки: является ли арендодатель - владельцем вещи
        if (itemDB.getOwner().getId().equals(bookerId)) {
            log.error("Ошибка! Невозможно добавить бронирование, пользователь по id={} " +
                    "является владельцем вещи", bookerId);
            throw new NotFoundException("Ошибка! Невозможно добавить бронирование!");
        }
    }

    public void checkBookerOrOwner(Long userId, Long bookingId) {
        // Метод проверки владельца вещи и клиента бронирования перед просмотром
        Booking booking = bookingRepository.findById(bookingId).get();
        Item item = booking.getItem();
        if (item.getOwner().getId().equals(userId)) {
            return;
        } else if (booking.getBooker().getId().equals(userId)) {
            return;
        }
        log.error("Просмотр запрещен! Пользователь по Id: {} не является ни владельцем вещи ни клиентом " +
                "бронирования!", userId);
        throw new NotFoundException("Просматривать информацию о бронированнии вещи может только владелец " +
                "или клиент бронирования!");
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
            log.error("Ошибка! Дата и время окончания бронирования не может предшествовать дате и времени начала бронирования!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            log.error("Ошибка! Дата и время начала бронирования равно дате и времени окончания бронирования!");
            throw new ValidateException("Ошибка! Дата и время начала бронирования должно отличатся " +
                    "от даты и времени окончания бронирования!");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            log.error("Ошибка! Указано неправильно дата/время начала бронирования!");
            throw new ValidateException("Ошибка! Дата и время начала бронирования должно " +
                    "быть позже точного времени!");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            log.error("Ошибка! Указано неправильно дата/время окончания бронирования!");
            throw new ValidateException("Ошибка! Дата и время окончания бронирования должно " +
                    "быть позже точного времени!");
        }

    }

    public void checkStatusState(String state) {
        if (state.equalsIgnoreCase(StatusState.ALL.name())) {
            return;
        }
        if (state.equalsIgnoreCase(StatusState.CURRENT.name())) {
            return;
        }
        if (state.equalsIgnoreCase(StatusState.PAST.name())) {
            return;
        }
        if (state.equalsIgnoreCase(StatusState.FUTURE.name())) {
            return;
        }
        if (state.equalsIgnoreCase(StatusState.WAITING.name())) {
            return;
        }
        if (state.equalsIgnoreCase(StatusState.REJECTED.name())) {
            return;
        }
        log.error("Ошибка! Указан неправильно статус бронирования!");
        throw new StateStatusValidateException();
    }

    public void checkTheUserRentedTheItem(Long userId, Item item) {
        List<Booking> bookings = item.getBookings();
        boolean isBooker = false;
        for (Booking booking : bookings) {
            Long bookerIdFromBooking = booking.getBooker().getId();
            if (bookerIdFromBooking.equals(userId) && booking.getEnd().isBefore(LocalDateTime.now())) {
                isBooker = true;
                break;
            }
        }
        if (!isBooker) {
            log.error("Ошибка! Cохранение комментария к вещи с id ={}", item.getId());
            throw new ValidateException("Пользователь по id=" + userId + " не арендовал эту вещь");
        }
    }

    public void checkCommentText(String text) { // Метод проверки поля text
        if (text == null || text.isBlank()) {
            throw new ValidateException("Текст комментария не может быть пустым.");
        }
    }
}

