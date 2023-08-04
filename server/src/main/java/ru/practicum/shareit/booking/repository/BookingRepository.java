package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.StatusDto;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long owner, Pageable page);

    @Query("select b from Booking  b where b.item.owner.id = ?1 and b.end > now() and b.start < now() " +
            "order by b.start desc ")
    List<Booking> findAllByItemOwnerAndStateCurrent(Long owner, Pageable page);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.end < now() and b.status =?2 " +
            "order by b.start desc ")
    List<Booking> findAllByItemOwnerIdAndStatePast(Long owner, StatusDto approved, Pageable page);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long owner, StatusDto status, Pageable page);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start > now() order by b.start desc ")
    List<Booking> findAllByBookerIdAndStateFuture(Long userId, Pageable page);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start < now() and b.end > now()")
    List<Booking> findAllByBookerIdAndStateCurrent(Long userId, Pageable page);

    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 and b.end < now() " +
            "order by b.start desc ")
    List<Booking> findAllByBookerIdAndStatePast(Long userId, StatusDto approved, Pageable page);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, StatusDto waiting, Pageable page);

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId, Pageable page);

    @Query("SELECT b FROM Booking b INNER JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = ?1 and not b.status =?2 ORDER BY b.start DESC")
    List<Booking> findAllByItemOwnerIdAndStateFuture(Long owner, StatusDto rejected, Pageable page);

    @Query("select b from Booking b where b.start >= now() and b.end > now() and b.item.id=?1" +
            " and (b.status =?2 or b.status =?3) order by b.start ASC ")
    List<Booking> findNextBookingByDate(Long itemId, StatusDto approved, StatusDto waiting, Pageable pageable);

    @Query("select b from Booking b where b.start < now() and b.item.id=?1" +
            " and (b.status =?2 or b.status =?3) order by b.start DESC ")
    List<Booking> findLastBookingByDate(Long itemId, StatusDto approved, StatusDto waiting, Pageable pageable);

}
