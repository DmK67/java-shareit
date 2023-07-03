package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBooking;

@Component
public class ItemMapper {
    public static ItemDto toItemDto(Item item) { // Метод перевода объекта item в объект itemDto
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem(ItemDto itemDto) { // Метод перевода объекта itemDto в объект item
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static ItemWithBooking itemWithBooking(Item item) {
        return ItemWithBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static ItemDtoForBooking itemWithItemDtoForBooking(Item item) {
        return ItemDtoForBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }



}
