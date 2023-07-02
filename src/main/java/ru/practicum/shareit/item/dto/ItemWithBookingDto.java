package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingForItemDto;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItemWithBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;

    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;
    private Long requestId;

    public void addLastAndNextBooking(BookingForItemDto lastBookingNew, BookingForItemDto nextBookingNew) {
        if (lastBookingNew == null) {
            lastBooking = nextBookingNew;
            nextBookingNew = null;
        } else {
            lastBooking = lastBookingNew;
            nextBooking = nextBookingNew;
        }
    }

}

