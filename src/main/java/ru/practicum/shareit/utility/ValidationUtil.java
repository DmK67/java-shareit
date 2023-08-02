package ru.practicum.shareit.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusState;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.StateStatusValidateException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
@Slf4j
public class ValidationUtil {

    public static void checkUniqueEmailUserAdd(User user) { // Метод проверки поля e-mail на пустые строки
        // и пробелы при добавлении
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Ошибка! Пользователь с пустым e-mail не может быть добавлен!");
            throw new ValidateException("Ошибка! Пользователь с пустым e-mail не может быть добавлен!");
        }
    }

    public void checkBookerIsTheOwner(Item itemDB, Long bookerId) { // Метод проверки: является ли арендодатель
        // - владельцем вещи
        if (itemDB.getOwner().getId().equals(bookerId)) {
            log.error("Ошибка! Невозможно добавить бронирование, пользователь по id={} " +
                    "является владельцем вещи", bookerId);
            throw new NotFoundException("Ошибка! Невозможно добавить бронирование!");
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

    public void checkBookingDtoWhenAdd(BookingDto bookingDto) {
        // Метод проверки полей объекта BookingDto перед добавлением
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            log.error("Ошибка! Поля начала и окончания бронирования не могут быть пустыми!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            log.error("Ошибка! Дата и время окончания бронирования не может предшествовать дате и времени начала " +
                    "бронирования!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            log.error("Ошибка! Дата и время начала бронирования равно дате и времени окончания бронирования!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            log.error("Ошибка! Указано неправильно дата/время начала бронирования!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            log.error("Ошибка! Указано неправильно дата/время окончания бронирования!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
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

