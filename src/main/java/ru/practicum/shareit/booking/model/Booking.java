package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
@Entity
@Table(name = "booking", schema = "public")
public class Booking {
    /**
     * id — уникальный идентификатор бронирования;
     * start — дата и время начала бронирования;
     * end — дата и время конца бронирования;
     * item — вещь, которую пользователь бронирует;
     * booker — пользователь, который осуществляет бронирование;
     * status — статус бронирования. Может принимать одно из следующих значений:
     * WAITING — новое бронирование, ожидает одобрения,
     * APPROVED — бронирование подтверждено владельцем,
     * REJECTED — бронирование отклонено владельцем,
     * CANCELED — бронирование отменено создателем.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_booking")
    private LocalDateTime start;
    @Column(name = "end_booking")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User booker;
    @Column()
    @Enumerated(EnumType.STRING)
    private Status status;

}

