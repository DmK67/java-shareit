package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

@Service
@AllArgsConstructor
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
}
