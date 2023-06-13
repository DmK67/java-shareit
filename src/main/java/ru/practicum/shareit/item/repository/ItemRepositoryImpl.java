package ru.practicum.shareit.item.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;

@Repository
@Data
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Long, Item> itemMap = new HashMap<>();
    private Long count = 0L;

    @Override
    public Item add(Item item) { // Метод добавление вещи
        item.setId(++count);
        log.info("Вещь: {} под id={} успешно добавлена!", item.getName(), item.getId());
        itemMap.put(item.getId(), item);
        return itemMap.get(item.getId());
    }

    @Override
    public Item getItemById(long id) { // Метод получения вещи по id
        if (itemMap.containsKey(id)) {
            log.info("Вывод вещи по id={}", id);
            return itemMap.get(id);
        } else {
            log.error("Вещь по id={} отсутствует в памяти.", id);
            throw new NotFoundException("Вещь по id=" + id + " не существует");
        }
    }

    @Override
    public Item updateItem(Item item) { // Метод обновления вещи
        itemMap.put(item.getId(), item);
        log.info("Вещь: {} успешно обновлена", itemMap.get(item.getId()));
        return itemMap.get(item.getId());
    }

    @Override
    public List<ItemDto> getListItemsUserById(Long ownerId) { // Метод получения списка вещей пользователя по id
        List<ItemDto> listItemsDtoUser = new ArrayList<>();
        for (Item item : itemMap.values()) {
            if (item.getOwner().equals(ownerId)) {
                listItemsDtoUser.add(toItemDto(item));
            }
        }
        log.info("Вывод вещей поьзователя по id={}", ownerId);
        return listItemsDtoUser;
    }

    @Override
    public List<ItemDto> getSearchItems(String text) { // Метод поиска вещей по строке
        text = text.toLowerCase();
        List<ItemDto> listItemDtoResult = new ArrayList<>();
        for (Item item : itemMap.values()) {
            if ((item.getName().toLowerCase().contains(text) && item.getAvailable())
                    || (item.getDescription().toLowerCase().contains(text) && item.getAvailable())) {
                listItemDtoResult.add(toItemDto(item));
            }
        }
        log.info("Вывод списка вещей удовлетворяющих поиску");
        return listItemDtoResult;
    }
}
