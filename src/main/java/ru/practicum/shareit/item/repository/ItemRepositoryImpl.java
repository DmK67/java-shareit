package ru.practicum.shareit.item.repository;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
@Data
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

    @Override
    public Item updateItem(Item item) {
        Item updateItem = itemMap.get(item.getId());
        if (item.getName() == null) {
            item.setName(updateItem.getName());
        }
        if (!item.getName().isBlank() && !updateItem.getName().equals(item.getName())) {
            updateItem.setName(item.getName());
        }
        if (!item.getDescription().isBlank() && !updateItem.getDescription().equals(item.getDescription())) {
            updateItem.setDescription(item.getDescription());
        }
        if (updateItem.isAvailable() != item.isAvailable()) {
            updateItem.setAvailable(item.isAvailable());
        }
        if (updateItem.getOwner() != item.getOwner()) {
            updateItem.setOwner(item.getOwner());
        }

        itemMap.remove(item.getId());
        itemMap.put(updateItem.getId(), updateItem);
        return itemMap.get(updateItem.getId());

//        if (user.getEmail() == null) {
//            user.setEmail(updateUser.getEmail());
//        }
//        if (!user.getEmail().isBlank() && !updateUser.getEmail().equals(user.getEmail())) {
//            updateUser.setEmail(user.getEmail());
//        }
//        userMap.remove(user.getId());
//        userMap.put(updateUser.getId(), updateUser);
//        return userMap.get(updateUser.getId());
    }
}
