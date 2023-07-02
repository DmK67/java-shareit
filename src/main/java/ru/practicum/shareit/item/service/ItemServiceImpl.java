package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingForItemDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.itemWithBooking;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.item.mapper.ItemWithBookingDtoMapper.toItemWithBookingDto;

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
    public ItemWithBookingDto getItemByIdWithBooking(Long itemId, Long owner) { // Метод получения вещи по id с бронированием
        Item itemFromBD = getItemById(itemId);
        User ownerFromBD = userService.getUserById(owner);

        ItemWithBooking itemWithBooking = itemWithBooking(itemFromBD);
        List<Booking> allBookings = itemFromBD.getBookings();
        Booking lastBooking = null;
        Booking nextBooking = null;
        LocalDateTime now = LocalDateTime.now();
        Long ownerIdFromItemFromBD = itemFromBD.getOwner().getId();      //ID хозяина вещи из БД.
        if (ownerIdFromItemFromBD.equals(owner) && allBookings != null) {
            nextBooking = findNextBookingByDate(allBookings, now);
            lastBooking = findLastBookingByDate(allBookings, now);
        }
        if (nextBooking == null || lastBooking == null) {
            ItemWithBookingDto itemWithBookingDto = toItemWithBookingDto(itemWithBooking);
            return itemWithBookingDto;
        } else {
            ItemWithBookingDto itemWithBookingDto = toItemWithBookingDto(itemWithBooking);
            BookingForItemDto nextBookingForItemDto = toBookingForItemDto(nextBooking);
            BookingForItemDto lastBookingForItemDto = toBookingForItemDto(lastBooking);
            itemWithBookingDto.setNextBooking(nextBookingForItemDto);
            itemWithBookingDto.setLastBooking(lastBookingForItemDto);
            return itemWithBookingDto;
        }
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

    private Booking findNextBookingByDate(List<Booking> bookings, LocalDateTime now) {
        // Метод поиска следующего бронирования после указанной даты
        Booking first = null;
        if (bookings != null && !bookings.isEmpty()) {
            for (Booking b : bookings) {
                if (b.getEnd().isAfter(now)) {
                    if (first == null && (b.getStatus().equals(Status.APPROVED)
                            || b.getStatus().equals(Status.WAITING))) {
                        first = b;
                    } else if (first == null) {
                        first = b;
                    } else if (b.getStart().isBefore(first.getStart())) {
                        first = b;
                    }
                }
            }
        }
        return first;
    }

    private Booking findLastBookingByDate(List<Booking> bookings, LocalDateTime now) {
        // Метод поиска последнего бронирования после указанной даты
        Booking last = null;
        if (bookings != null && !bookings.isEmpty()) {
            for (Booking b : bookings) {
                if (b.getEnd().isBefore(now)) {
                    if (last == null && (b.getStatus().equals(Status.APPROVED))) {
                        last = b;
                    } else if (last == null) {
                        last = b;
                    } else if (b.getEnd().isAfter(last.getEnd())) {
                        last = b;
                    }
                }
            }
        }
        return last;
    }

}
