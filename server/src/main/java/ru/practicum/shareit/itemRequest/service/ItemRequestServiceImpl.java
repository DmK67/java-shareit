package ru.practicum.shareit.itemRequest.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.itemRequest.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.itemRequest.mapper.ItemRequestMapper.toItemRequestDtoWithAnswers;
import static ru.practicum.shareit.itemRequest.mapper.ItemRequestMapper.toListItemRequestDtoWithAnswers;

@Service
@AllArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private UserRepository userRepository;
    private ItemRequestRepository itemRequestRepository;
    private ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemRequest addItemRequest(ItemRequest itemRequest, Long requesterId) { // Метод добавления запроса на вещь
        userRepository.findById(requesterId) // Проверяем пользователя по id на существование в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + requesterId + " не существует!"));
        itemRequest.setRequestor(User.builder().id(requesterId).build());
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest itemRequestSaved = itemRequestRepository.save(itemRequest);
        return itemRequestSaved;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDtoWithAnswers> getItemRequestsByUserId(Long requesterId) {
        //  Метод получения списка своих запросов вместе с ответами на них
        userRepository.findById(requesterId) // Проверяем пользователя по id на существование в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + requesterId + " не существует!"));
        List<ItemRequest> itemRequests = itemRequestRepository.getAllByRequestorIdOrderByCreatedDesc(requesterId);
        log.info("Получен список запросов пользователя с ID = '{}' .", requesterId);
        List<Long> listRequestId = itemRequests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        List<Item> allByRequestIds = itemRepository.findAllByRequestIds(listRequestId);
        addItems(allByRequestIds, itemRequests);
        return toListItemRequestDtoWithAnswers(itemRequests);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDtoWithAnswers> getListRequestsCreatedByOtherUsers(Long requesterId, Integer from,
                                                                              Integer size) {
        // Метод получения списка запросов, созданных другими пользователями
        userRepository.findById(requesterId) // Проверяем пользователя по id на существование в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + requesterId + " не существует!"));
        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemRequest> itemRequests =
                itemRequestRepository.getItemRequestByRequesterIdIsNotOrderByCreated(requesterId, pageable);
        List<Long> listRequestId = itemRequests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        List<Item> allByRequestIds = itemRepository.findAllByRequestIds(listRequestId);
        addItems(allByRequestIds, itemRequests);
        log.info("Получен список всех запросов для пользователя по Id = {}.", requesterId);
        return toListItemRequestDtoWithAnswers(itemRequests);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestDtoWithAnswers getItemRequestById(Long userId, Long itemRequestId) {
        userRepository.findById(userId) // Проверяем пользователя по id на существование в БД
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + userId + " не существует!"));
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId).orElseThrow(
                () -> new NotFoundException("Запрос на вещь по id=" + itemRequestId + " не существует!"));
        log.info("Получен запрос на вещь по Id ={} ", itemRequestId);
        List<Item> listItem = itemRepository.findAllByRequestId(itemRequestId);
        itemRequest.setItems(listItem);
        return toItemRequestDtoWithAnswers(itemRequest);
    }

    private void addItems(List<Item> allByRequestIds, List<ItemRequest> itemRequests) {
        if (!allByRequestIds.isEmpty()) {
            Map<Long, List<Item>> mapItem = allByRequestIds.stream()
                    .collect(Collectors.groupingBy(item -> item.getRequestId(), Collectors.toList()));

            itemRequests.stream()
                    .forEach(request -> {
                        List<Item> listItem = mapItem.getOrDefault(request.getId(), List.of());

                        request.setItems(listItem);
                    });
        }
    }

}
