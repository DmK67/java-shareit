package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;
    private final ValidationService validationService;

    @Override
    public Item addItem(Item item, Long ownerId) {
        validationService.checkItemDtoWhenAdd(toItemDto(item)); // Проверяем поля объекта itemDto перед добавлением
        item.setOwner(userService.getUserById(ownerId));
        return repository.save(item);
    }

    @Override
    public Item getItemById(Long id) { // Метод получения вещи по id
        Item item = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь по id=" + id + " не существует!"));
        return item;
    }

    @Override
    public Item updateItem(Item item, Long itemId, Long ownerId) { // Метод обновления вещи
        getItemById(itemId); // Проверяем вещь по id на существование в БД
        userService.getUserById(ownerId); // Проверяем пользователя по id на существование в БД
        item.setOwner(userService.getUserById(ownerId));
        validationService.checkOwnerItem(itemId, ownerId); // Проверяем соответствие владельца вещи

        Item updateItem = getItemById(itemId);
        if (item.getName() == null) {
            item.setName(updateItem.getName());
        }
        if (!item.getName().isBlank() && !updateItem.getName().equals(item.getName())) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(updateItem.getDescription());
        }
        if (!item.getDescription().isBlank() && !updateItem.getDescription().equals(item.getDescription())) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(updateItem.getAvailable());
        }
        if (updateItem.getAvailable() != item.getAvailable()) {
            updateItem.setAvailable(item.getAvailable());
        }
        if (!updateItem.getOwner().equals(item.getOwner())) {
            updateItem.setOwner(item.getOwner());
        }
        return repository.save(updateItem);
    }

    @Override
    public List<ItemDto> getListItemsUserById(Long ownerId) { // Метод получения списка вещей по id пользователя
        User owner = userService.getUserById(ownerId); // Проверяем владельца вещи по id на существование в памяти
        return convertListItemsToListItemsDto(repository.findAllByOwnerOrderById(owner));
    }

    @Override
    public List<ItemDto> getSearchItems(String text) { // Метод поиска вещей по подстроке
        if (text == null || text.isBlank()) { // Проверка на пустою строку и на null
            log.error("В поиск передано пустое значение {}!", text);
            return Collections.emptyList();
        }
        log.info("Осуществляем поиск вещи содержащее текст: {}.", text);
        return convertListItemsToListItemsDto(repository.searchItemsByNameContaining(text));
    }

    @Override
    public void checkingIsAvailable(Item item) { // Метод проверки статуса бронирования
        if (!item.getAvailable()) {
            throw new ValidateException("Вещь нельзя забронировать, поскольку available = false.");
        }
    }

    private List<ItemDto> convertListItemsToListItemsDto(List<Item> listItems) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : listItems) {
            itemDtoList.add(toItemDto(item));
        }
        return itemDtoList;
    }


}
