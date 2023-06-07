package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    Map<Long, Item> itemMap = new HashMap<>();
    private Long count = 0L;

    @Override
    public Item add(Item item) {
        item.setId(++count);
        itemMap.put(item.getId(), item);
        return itemMap.get(item.getId());
    }

    @Override
    public Item getItemById(long id) {
        if (itemMap.containsKey(id)) {
            return itemMap.get(id);
        } else {
            throw new NotFoundException("Вещь по id=" + id + " не существует");
        }
    }
}
