package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.ItemWithBooking;

@Component
public class ItemWithBookingDtoMapper {

    public static ItemWithBookingDto toItemWithBookingDto(ItemWithBooking itemWithBooking) { // Метод перевода объекта item в объект itemDto
        return ItemWithBookingDto.builder()
                .id(itemWithBooking.getId())
                .name(itemWithBooking.getName())
                .description(itemWithBooking.getDescription())
                .available(itemWithBooking.getAvailable())
                .lastBooking(itemWithBooking.getLastBooking())
                .nextBooking(itemWithBooking.getNextBooking())
                .build();
    }

    public static ItemWithBooking toItemWithBooking(ItemWithBookingDto itemWithBookingDto) { // Метод перевода объекта itemDto в объект item
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
///**
//     * Из объекта для ответа в вещь.
//     */
//
//    Item mapToModel(ItemWithBookingDto itemWithBookingDto);
//
//    /**
//     * Из вещи в объект для ответа.
//     */
//    ItemWithBookingDto mapToDto(Item item);