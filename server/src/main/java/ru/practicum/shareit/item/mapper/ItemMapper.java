package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBooking;

import java.util.ArrayList;
import java.util.List;


public class ItemMapper {
    public static ItemDto toItemDto(Item item) { // Метод перевода объекта item в объект itemDto
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId() != null ? item.getRequestId() : null)
                .build();
    }

    public static Item toItem(ItemDto itemDto) { // Метод перевода объекта itemDto в объект item
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .requestId(itemDto.getRequestId() != null ? itemDto.getRequestId() : null)
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

    public static List<ItemDto> convertListItemsToListItemsDto(List<Item> listItems) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : listItems) {
            itemDtoList.add(toItemDto(item));
        }
        return itemDtoList;
    }

    public static List<ItemForResponseDto> convertListItemsToListIItemForResponseDto(List<Item> listItems) {
        List<ItemForResponseDto> itemForResponseDtoList = new ArrayList<>();
        for (Item item : listItems) {
            itemForResponseDtoList.add(toItemForResponseDto(item));
        }
        return itemForResponseDtoList;
    }

    public static ItemForResponseDto toItemForResponseDto(Item item) {
        // Метод перевода объекта item в объект itemForResponseDto
        return ItemForResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

}
