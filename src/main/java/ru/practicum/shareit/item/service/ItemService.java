package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(Item item, Long ownerId);

    Item getItemById(Long id, Long ownerId);

    Item updateItem(Item item, Long id, Long ownerId);

    List<ItemDto> getListItemsUserById(Long ownerId);

    List<ItemDto> getSearchItems(String text);
}
