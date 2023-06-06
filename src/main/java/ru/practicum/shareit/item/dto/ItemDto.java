package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Booking available;
    private User owner;
    private ItemRequest request;
}
