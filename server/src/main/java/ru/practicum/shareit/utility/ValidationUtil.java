package ru.practicum.shareit.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.StateDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.StateStatusValidateException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
@Slf4j
public class ValidationUtil {

    public void checkBookerIsTheOwner(Item itemDB, Long bookerId) { // Метод проверки: является ли арендодатель
        // - владельцем вещи
        if (itemDB.getOwner().getId().equals(bookerId)) {
            log.error("Ошибка! Невозможно добавить бронирование, пользователь по id={} " +
                    "является владельцем вещи", bookerId);
            throw new NotFoundException("Ошибка! Невозможно добавить бронирование!");
        }
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

}

