package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(BookingDto bookingDto, Long bookerId);

    Booking getBookingById(Long id);

    Booking updateBooking(Booking booking, Long userId);

    void deleteBooking(Long id);

    List<Booking> getListBookingsUserById(Long userId, String state);

    Booking getBookingByIdAndStatus(Long ownerId, Long bookingId);

    Booking updateStatusBooking(Long ownerId, Boolean approved, Long bookingId);

    List<Booking> getListBookingsOwnerById(Long owner, String state);
}
