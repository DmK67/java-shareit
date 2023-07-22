package ru.practicum.shareit.itemRequest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.itemRequest.repository.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.List;

import static ru.practicum.shareit.itemRequest.mapper.ItemRequestMapper.toItemRequest;
import static ru.practicum.shareit.itemRequest.mapper.ItemRequestMapper.toItemRequestDto;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@AllArgsConstructor
@Validated
@Slf4j
//@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

/**Пользователь создаёт такой запрос, когда не может найти нужную вещь, воспользовавшись поиском, но при этом надеется,
 *  что у кого-то она всё же имеется. Другие пользователи могут просматривать подобные запросы и,
 *  если у них есть описанная вещь и они готовы предоставить её в аренду, добавлять нужную вещь в ответ на запрос.*/


    /**
     * POST /requests — добавить новый запрос вещи. Основная часть запроса — текст запроса, где пользователь описывает,
     * какая именно вещь ему нужна.
     */
    @PostMapping // Эндпоинт добавления запроса на вещь
    public ItemRequestDto addItemRequest(@Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id", required = false)
                                         Long requestorId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Добавление нового запроса на вещь. Запрос на = {}", itemRequestDto);
        return toItemRequestDto(itemRequestService.addItemRequest(toItemRequest(itemRequestDto), requestorId));
    }

    /**
     * GET /requests — получить список своих запросов вместе с данными об ответах на них. Для каждого запроса
     * должны указываться описание, дата и время создания и список ответов в формате: id вещи, название, id владельца.
     * Так в дальнейшем, используя указанные id вещей, можно будет получить подробную информацию о каждой вещи.
     * Запросы должны возвращаться в отсортированном порядке от более новых к более старым.
     */
    @GetMapping // Эндпоинт получения списка своих запросов вместе с данными об ответах на них
    public List<ItemRequestDtoWithAnswers> getItemRequestsByUserId(@RequestHeader("X-Sharer-User-Id")
                                                                   Long requesterId) {
        log.info("Получение списка запросов пользователя по Id = {}.", requesterId);
        return itemRequestService.getItemRequestsByUserId(requesterId);
    }

    /**
     * GET /requests/all?from={from}&size={size}` — получить список запросов, созданных другими пользователями.
     * С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
     * Запросы сортируются по дате создания: от более новых к более старым. Результаты должны возвращаться постранично.
     * Для этого нужно передать два параметра: `from` — индекс первого элемента, начиная с 0, и `size` — количество
     * элементов для отображения.
     */
    @GetMapping("/all") // Эндпоинт получения списока запросов, созданных другими пользователями
    public List<ItemRequestDtoWithAnswers> getAllRequests(
            @RequestHeader("X-Sharer-User-Id") Long requesterId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "20") Integer size) {
        log.info("Получение списка запросов, созданных другими пользователями кроме Id ={}.", requesterId);
        return itemRequestService.getListRequestsCreatedByOtherUsers(requesterId, from, size);
    }

    /**
     * GET /requests/{requestId}` — получить данные об одном конкретном запросе вместе с данными об ответах на него
     * в том же формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь.
     */
    @GetMapping("{requestId}") // Эндпоинт получения данных об одном конкретном запросе вместе с ответами на него
    public ItemRequestDtoWithAnswers getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @PathVariable Long requestId) {
        log.info("Получение запроса на вещь с определённым ID.");
        return itemRequestService.getItemRequestById(userId, requestId);
    }
}
