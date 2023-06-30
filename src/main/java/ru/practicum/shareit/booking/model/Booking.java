package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
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
     *
     * statusState — статус бронирования. Может принимать одно из следующих значений:
     * ALL (англ. «все»),
     * CURRENT (англ. «текущие»),
     * PAST (англ. «завершённые»),
     * FUTURE (англ. «будущие»),
     * WAITING (англ. «ожидающие подтверждения»),
     * REJECTED (англ. «отклонённые»).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(name = "start_booking")
    private LocalDateTime start;
    @Column(name = "end_booking")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;
    @Column(name = "booking_status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "booking_status_state")
    @Enumerated(EnumType.STRING)
    private StatusState statusState;

}

