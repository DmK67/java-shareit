package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.ItemWithBooking;

import java.util.ArrayList;

import static ru.practicum.shareit.item.comment.mapper.CommentMapper.convertListCommentsToListCommentsDto;


public class ItemWithBookingDtoMapper {

    public static ItemWithBookingDto toItemWithBookingDto(ItemWithBooking itemWithBooking) {
        // Метод перевода объекта item в объект itemDto
        return ItemWithBookingDto.builder()
                .id(itemWithBooking.getId())
                .name(itemWithBooking.getName())
                .description(itemWithBooking.getDescription())
                .available(itemWithBooking.getAvailable())
                .lastBooking(itemWithBooking.getLastBooking())
                .nextBooking(itemWithBooking.getNextBooking())
                .comments(itemWithBooking.getComments() != null ?
                        convertListCommentsToListCommentsDto(itemWithBooking.getComments()) : new ArrayList<>())
                .build();
    }

    public static ItemWithBooking toItemWithBooking(ItemWithBookingDto itemWithBookingDto) {
        // Метод перевода объекта itemDto в объект item
        return ItemWithBooking.builder()
                .id(itemWithBookingDto.getId())
                .name(itemWithBookingDto.getName())
                .description(itemWithBookingDto.getDescription())
                .available(itemWithBookingDto.getAvailable())
                .nextBooking(itemWithBookingDto.getNextBooking())
                .lastBooking(itemWithBookingDto.getLastBooking())
                .build();
    }
}
