package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.StatusState;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long owner);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item.id, b.booker, " +
            "b.status, b.item) " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 and b.end > now() and b.start < now() " +
            "order by b.start desc ")
    List<Booking> findAllByItemOwnerAndStateCurrent(Long owner);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item.id, b.booker, " +
            "b.status,b.item) " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 and b.end < now() and b.status =?2 " +
            "order by b.start desc ")
    List<Booking> findAllByItemOwnerIdAndStatePast(Long owner, Status approved);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item.id, b.booker, b.status," +
            " b.item) " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 and b.start > now() and not b.status =?2 " +
            "order by b.start desc ")
    List<Booking> findAllByItemOwnerIdAndStatusStateFuture(long userId, Status future);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long owner, Status status);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item.id, b.booker, b.status," +
            " b.item) " +
            "from Booking as b " +
            "where b.booker.id = ?1 and b.start > now() " +
            "order by b.start desc ")
    List<Booking> findAllByBookerIdAndStateFuture(Long owner);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item.id, b.booker, b.status, b.item) " +
            "from Booking as b " +
            "where b.booker.id = ?1 and b.end > now() and b.start < now() " +
            "order by b.start desc ")
    List<Booking> findAllByBookerIdAndStateCurrent(Long userId);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item.id, b.booker, b.status, b.item) " +
            "from Booking as b " +
            "where b.booker.id = ?1 and b.status = ?2 and b.end < now() " +
            "order by b.start desc ")
    List<Booking> findAllByBookerIdAndStatePast(Long userId, Status approved);


    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, Status waiting);


    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);
}
