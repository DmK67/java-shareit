package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
//@AllArgsConstructor
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private long request;
    private long ownerId;

    public ItemDto(long id, String name, String description, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
