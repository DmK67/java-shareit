package ru.practicum.shareit.itemRequest.service;

import ru.practicum.shareit.itemRequest.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.itemRequest.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest addItemRequest(ItemRequest itemRequest, Long requesterId);

    List<ItemRequestDtoWithAnswers> getItemRequestsByUserId(Long requesterId);

    List<ItemRequestDtoWithAnswers> getListRequestsCreatedByOtherUsers(Long requesterId, Integer from, Integer size);

    ItemRequestDtoWithAnswers getItemRequestById(Long userId, Long requestId);

}
