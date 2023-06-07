package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item add(Item item);

    Item getItemById (long id);

    //Item checkExistItemById (Item item);

}
