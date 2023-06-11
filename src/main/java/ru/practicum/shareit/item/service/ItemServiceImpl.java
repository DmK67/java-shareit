package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public Item addItem(Item item, Long ownerId) {
        item.setOwner(ownerId);
        return repository.add(item);
    }

    @Override
    public Item getItemById(long id) {
        return repository.getItemById(id);
    }

    @Override
    public Item updateItem(Item item) {
        return repository.updateItem(item);
    }

    @Override
    public List<ItemDto> getListItemsUserById(Long ownerId) {
        return repository.getListItemsUserById(ownerId);
    }

    @Override
    public List<ItemDto> getSearchItems(String text) {
        if (text == null || text.isBlank()) { // Проверка на пустою строку и на null
            log.error("В поиск передано пустое значение {}!", text);
            return Collections.emptyList();
        }
        log.info("Осуществляем поиск вещи содержащее текст: {}.", text);
        return repository.getSearchItems(text);
    }

}
