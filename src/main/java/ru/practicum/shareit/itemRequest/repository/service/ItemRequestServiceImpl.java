package ru.practicum.shareit.itemRequest.repository.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.itemRequest.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.itemRequest.mapper.ItemRequestMapper.toItemRequestDtoWithAnswers;
import static ru.practicum.shareit.itemRequest.mapper.ItemRequestMapper.toListItemRequestDtoWithAnswers;

@Service
@AllArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    @Override
    public ItemRequest addItemRequest(ItemRequest itemRequest, Long requesterId) { // Метод добавления запроса на вещь
        if (requesterId == null) { // Проверка аргумента requesterId на null
            throw new ValidateException("Неверный параметр пользователя (Id = " + null + ").");
        }
        userService.getUserById(requesterId); // Проверяем пользователя по id на существование в БД
        itemRequest.setRequestor(User.builder().id(requesterId).build());
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(itemRequest);
    }

    @Transactional
    @Override
    public List<ItemRequestDtoWithAnswers> getItemRequestsByUserId(Long requesterId) {
        //  Метод получения списка своих запросов вместе с ответами на них
        userService.getUserById(requesterId); // Проверяем пользователя по id на существование в БД
        List<ItemRequest> itemRequests = itemRequestRepository.getAllByRequestorIdOrderByCreatedDesc(requesterId);
        log.info("Получен список запросов пользователя с ID = '{}' .", requesterId);
        return toListItemRequestDtoWithAnswers(itemRequests);
    }

    @Transactional
    @Override
    public List<ItemRequestDtoWithAnswers> getListRequestsCreatedByOtherUsers(Long requesterId, Integer from,
                                                                              Integer size) {
        // Метод получения списка запросов, созданных другими пользователями
        if (from < 0) {
            log.error("Ошибка! Параметр from= {} не может быть отрицательным!", from);
            throw new ValidateException("Параметр from = " + from + " не может быть отрицательным!.");
        }
        if (size < 1) {
            log.error("Ошибка! Параметр size= {} не может быть меньше либо равно единице!", size);
            throw new ValidateException("Не верный параметр size = " + size + ".");
        }
        userService.getUserById(requesterId);  // Проверяем пользователя по id на существование в БД
        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemRequest> itemRequests =
                itemRequestRepository.getItemRequestByRequesterIdIsNotOrderByCreated(requesterId, pageable);
        log.info("Получен список всех запросов для пользователя по Id = {}.", requesterId);
        return toListItemRequestDtoWithAnswers(itemRequests);
    }

    @Transactional
    @Override
    public ItemRequestDtoWithAnswers getItemRequestById(Long userId, Long itemRequestId) {
        userService.getUserById(userId);  // Проверяем пользователя по id на существование в БД
        if (itemRequestId == null) {
            log.info("Ошибка! Значение Id= {} не может быть пустым!", itemRequestId);
            throw new ValidateException("Передан не верный Id=" + itemRequestId);
        }
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId).orElseThrow(
                () -> new NotFoundException("Запрос на вещь по id=" + itemRequestId + " не существует!"));
        log.info("Получен запрос на вещь по Id ={} ", itemRequestId);
        return toItemRequestDtoWithAnswers(itemRequest);
    }


}
