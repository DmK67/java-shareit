package ru.practicum.shareit.itemRequest.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.itemRequest.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        // Метод перевода объекта itemRequest в объект itemRequestDto
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        // Метод перевода объекта itemRequestDto в объект itemRequest
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestor(itemRequestDto.getRequestor())
                .build();
    }

    public static ItemRequestDtoWithAnswers toItemRequestDtoWithAnswers(ItemRequest itemRequest) {
        // Метод перевода объекта itemRequest в объект itemRequestDtoWithAnswers
        return ItemRequestDtoWithAnswers.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public static List<ItemRequestDtoWithAnswers> toListItemRequestDtoWithAnswers(List<ItemRequest> itemRequests) {
        // Метод перевода списка объектов itemRequest в список объектов itemRequestDtoWithAnswers
        List<ItemRequestDtoWithAnswers> list = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            list.add(toItemRequestDtoWithAnswers(itemRequest));
        }
        return list;
    }
}
