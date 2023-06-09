package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public interface ItemService {
    Item addItem(Item item, Long ownerId);

    Item getItemById(long id);

    Item updateItem(Item item);
}
