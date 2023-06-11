package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {
    public static ItemDto toItemDto(Item item) { // Метод перевода объекта item в объект itemDto
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner() != null ? item.getOwner() : null
        );
    }

    public static Item toItem(ItemDto itemDto) { // Метод перевода объекта itemDto в объект item
        return new Item(
                itemDto.getId() != null ? itemDto.getId() : null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwnerId() != null ? itemDto.getOwnerId() : null
        );
    }

}
