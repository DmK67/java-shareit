package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long owner);

    @Query("select b from Booking  b where b.item.owner.id = ?1 and b.end > now() and b.start < now() " +
            "order by b.start desc ")
    List<Booking> findAllByItemOwnerAndStateCurrent(Long owner);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.end < now() and b.status =?2 " +
            "order by b.start desc ")
    List<Booking> findAllByItemOwnerIdAndStatePast(Long owner, Status approved);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long owner, Status status);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start > now() order by b.start desc ")
    List<Booking> findAllByBookerIdAndStateFuture(Long userId);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start < now() and b.end > now()")
    List<Booking> findAllByBookerIdAndStateCurrent(Long userId);

    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 and b.end < now() " +
            "order by b.start desc ")
    List<Booking> findAllByBookerIdAndStatePast(Long userId, Status approved);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, Status waiting);

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    @Query("SELECT b FROM Booking b INNER JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = ?1 and not b.status =?2 ORDER BY b.start DESC")
    List<Booking> findAllByItemOwnerIdAndStateFuture(Long owner, Status rejected);

}
