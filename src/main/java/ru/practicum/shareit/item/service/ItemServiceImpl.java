package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repositiry.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingForItemDto;
import static ru.practicum.shareit.item.comment.mapper.CommentMapper.convertListCommentsToListCommentsDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;
import static ru.practicum.shareit.item.mapper.ItemWithBookingDtoMapper.toItemWithBookingDto;
import static ru.practicum.shareit.utility.ValidationClass.*;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public Item addItem(Item item, Long ownerId) {
        checkItemDtoWhenAdd(toItemDto(item)); // Проверяем поля объекта itemDto перед добавлением
        User userFromBd = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + ownerId + " не существует!"));
        item.setOwner(userFromBd);
        return repository.save(item);
    }

    @Transactional(readOnly = true)
    @Override
    public Item getItemById(Long id) { // Метод получения вещи по id
        Item item = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь по id=" + id + " не существует!"));
        return item;
    }

    @Transactional(readOnly = true)
    @Override
    public ItemWithBookingDto getItemByIdWithBooking(Long itemId, Long owner) {
        // Метод получения вещи по id с бронированием
        Item itemFromBD = getItemById(itemId);
        userRepository.findById(owner) // Проверяем пользователя по id на существование в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + owner + " не существует!"));
        ItemWithBooking itemWithBooking = itemWithBooking(itemFromBD);
        List<Booking> allBookings = itemFromBD.getBookings();
        Booking lastBooking = null;
        Booking nextBooking = null;
        Long ownerIdFromItemFromBD = itemFromBD.getOwner().getId();
        if (ownerIdFromItemFromBD.equals(owner) && allBookings != null) {
            nextBooking = findNextBookingByDate(itemId);
            lastBooking = findLastBookingByDate(itemId);
        }
        ItemWithBookingDto itemWithBookingDto = toItemWithBookingDto(itemWithBooking);
        List<Comment> listComments = itemFromBD.getComments();
        if (nextBooking == null && lastBooking == null) {
            List<CommentDto> commentDtoList = convertListCommentsToListCommentsDto(listComments);
            itemWithBookingDto.setComments(commentDtoList);
            return itemWithBookingDto;
        } else {
            BookingForItemDto nextBookingForItemDto = toBookingForItemDto(nextBooking);
            BookingForItemDto lastBookingForItemDto = toBookingForItemDto(lastBooking);
            itemWithBookingDto.setNextBooking(nextBookingForItemDto);
            itemWithBookingDto.setLastBooking(lastBookingForItemDto);
            List<CommentDto> commentDtoList = convertListCommentsToListCommentsDto(listComments);
            itemWithBookingDto.setComments(commentDtoList);
            return itemWithBookingDto;
        }
    }

    @Transactional
    @Override
    public Item updateItem(Item item, Long itemId, Long ownerId) { // Метод обновления вещи
        Item updateItem = getItemById(itemId); // Проверяем вещь по id на существование в БД
        User owner = userRepository.findById(ownerId) // Проверяем пользователя по id на существование в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + ownerId + " не существует!"));
        item.setOwner(owner);
        if (!updateItem.getOwner().getId().equals(ownerId)) { // Проверяем соответствие владельца вещи
            log.error("Ошибка! Пользователь по Id: {} не является владельцем вещи! " +
                    "Изменение вещи ЗАПРЕЩЕНО!", ownerId);
            throw new ForbiddenException("Вносить изменения в параметры вещи может только владелец!");
        }
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

    @Transactional(readOnly = true)
    @Override
    public List<ItemWithBookingDto> getListItemsUserById(Long ownerId, Integer from, Integer size) {
        // Метод получения списка вещей по id пользователя
        userRepository.findById(ownerId) // Проверяем пользователя по id на существование в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + ownerId + " не существует!"));
        Pageable page = PageRequest.of(from / size, size);
        List<Item> itemList = repository.findAllByOwnerId(ownerId, page);
        List<ItemWithBookingDto> itemWithBookingDtoList = new ArrayList<>();
        for (Item item : itemList) {
            ItemWithBooking itemWithBooking = itemWithBooking(item);
            Booking lastBooking = null;
            Booking nextBooking = null;
            if (item.getOwner().getId().equals(ownerId) && item.getBookings() != null) {
                nextBooking = findNextBookingByDate(item.getId());
                lastBooking = findLastBookingByDate(item.getId());
            }
            List<Comment> listComments = item.getComments();
            ItemWithBookingDto itemWithBookingDto = toItemWithBookingDto(itemWithBooking);
            if (nextBooking == null && lastBooking == null) {
                List<CommentDto> commentDtoList = convertListCommentsToListCommentsDto(listComments);
                itemWithBookingDto.setComments(commentDtoList);
                itemWithBookingDtoList.add(itemWithBookingDto);
            } else {
                BookingForItemDto nextBookingForItemDto = toBookingForItemDto(nextBooking);
                BookingForItemDto lastBookingForItemDto = toBookingForItemDto(lastBooking);
                itemWithBookingDto.setNextBooking(nextBookingForItemDto);
                itemWithBookingDto.setLastBooking(lastBookingForItemDto);
                List<CommentDto> commentDtoList = convertListCommentsToListCommentsDto(listComments);
                itemWithBookingDto.setComments(commentDtoList);
                itemWithBookingDtoList.add(itemWithBookingDto);
            }
        }
        return itemWithBookingDtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getSearchItems(String text, Integer from, Integer size) { // Метод поиска вещей по подстроке
        Pageable page = PageRequest.of(from / size, size);
        if (text == null || text.isBlank()) { // Проверка на пустою строку и на null
            log.error("В поиск передано пустое значение {}!", text);
            return Collections.emptyList();
        }
        log.info("Осуществляем поиск вещи содержащее текст: {}.", text);
        return convertListItemsToListItemsDto(repository.searchItemsByNameContaining(text, page));
    }

    @Override
    public void checkingIsAvailable(Item item) { // Метод проверки статуса бронирования
        if (!item.getAvailable()) {
            throw new ValidateException("Вещь нельзя забронировать, поскольку available = false.");
        }
    }

    @Transactional
    @Override
    public Comment addComment(Comment comment, Long ownerId, Long itemId) { // Метод добавления комментария
        Item item = getItemById(itemId); // Проверяем вещь по id на существование в БД
        User user = userRepository.findById(ownerId) // Проверяем пользователя по id на существование в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + ownerId + " не существует!"));
        checkCommentText(comment.getText()); // Проверяем поле text
        checkTheUserRentedTheItem(ownerId, item); // Проверяем что пользователь действительно брал
        // вещь в аренду
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        Comment commentFromBd = commentRepository.save(comment);
        item.setComments(new ArrayList<>(List.of(commentFromBd)));
        return commentFromBd;
    }

    private Booking findNextBookingByDate(Long itemId) {
        // Метод поиска следующего бронирования после указанной даты
        Booking nextBooking = null;
        List<Booking> listNextBookings = bookingRepository.findNextBookingByDate(itemId, Status.APPROVED,
                Status.WAITING, PageRequest.of(0, 1));
        if (!listNextBookings.isEmpty()) {
            nextBooking = listNextBookings.get(0);
        }
        return nextBooking;
    }

    private Booking findLastBookingByDate(Long itemId) {
        // Метод поиска последнего бронирования после указанной даты
        Booking lastBooking = null;
        List<Booking> listNextBookings = bookingRepository.findLastBookingByDate(itemId, Status.APPROVED,
                Status.WAITING, PageRequest.of(0, 1));
        if (!listNextBookings.isEmpty()) {
            lastBooking = listNextBookings.get(0);
        }
        return lastBooking;
    }


}
