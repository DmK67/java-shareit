package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDto bookingDto, Long bookerId);

    BookingDto getBookingById(Long id);

    void deleteBooking(Long id);

    List<BookingDto> getListBookingsUserById(Long userId, String state, Integer from, Integer size);

    BookingDto getBookingByIdAndStatus(Long ownerId, Long bookingId);

    BookingDto updateBooking(Long ownerId, Boolean approved, Long bookingId);

    List<BookingDto> getListBookingsOwnerById(Long owner, String state, Integer from, Integer size);
}
