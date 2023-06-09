package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Map;

public interface ItemRepository {
    Item add(Item item);

    Item getItemById(long id);

    Item updateItem(Item item);

    Map<Long, Item> getItemMap();

}
