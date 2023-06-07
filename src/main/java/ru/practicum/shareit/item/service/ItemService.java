package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

public interface ItemService {
    Item addItem(Item item, Long ownerId);

    Item getItemById(long id);
}
