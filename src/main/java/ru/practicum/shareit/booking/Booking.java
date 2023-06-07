package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    //id — уникальный идентификатор бронирования;
    //start — дата и время начала бронирования;
    //end — дата и время конца бронирования;
    //item — вещь, которую пользователь бронирует;
    //booker — пользователь, который осуществляет бронирование;
    //status — статус бронирования. Может принимать одно из следующих
    //значений: WAITING — новое бронирование, ожидает одобрения, APPROVED —
    //бронирование подтверждено владельцем, REJECTED — бронирование
    //отклонено владельцем, CANCELED — бронирование отменено создателем.
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status;
}
enum Status {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED
}
