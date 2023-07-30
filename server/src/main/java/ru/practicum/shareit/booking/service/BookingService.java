package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(BookingDto bookingDto, Long bookerId);

    Booking getBookingById(Long id);

    void deleteBooking(Long id);

    List<Booking> getListBookingsUserById(Long userId, String state, Integer from, Integer size);

    Booking getBookingByIdAndStatus(Long ownerId, Long bookingId);

    Booking updateBooking(Long ownerId, Boolean approved, Long bookingId);

    List<Booking> getListBookingsOwnerById(Long owner, String state, Integer from, Integer size);
}
