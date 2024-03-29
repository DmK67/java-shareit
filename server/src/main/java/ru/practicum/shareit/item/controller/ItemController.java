package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.item.comment.mapper.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    /**
     * Добавление новой вещи. Будет происходить по эндпойнту POST /items. На вход поступает объект ItemDto.
     * userId в заголовке X-Sharer-User-Id — это идентификатор пользователя, который добавляет вещь.
     * Именно этот пользователь — владелец вещи. Идентификатор владельца будет поступать на вход в каждом из запросов,
     * рассмотренных далее.
     */

    @PostMapping // Эндпоинт добавления вещи
    public ItemDto addItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                           @RequestBody ItemDto itemDto) {
        log.info("Добавляем вещь: {}", itemDto.getName());
        return toItemDto(itemService.addItem(toItem(itemDto), ownerId));
    }

    /**
     * Редактирование вещи. Эндпойнт PATCH /items/{itemId}. Изменить можно название, описание и статус доступа к аренде.
     * Редактировать вещь может только её владелец.
     */
    @PatchMapping("/{itemId}") // Эндпоинт обновления вещи по id
    public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id", required = false)
                              Long ownerId,
                              @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("Обновляем вещь по Id={}", itemId);
        return toItemDto(itemService.updateItem(toItem(itemDto), itemId, ownerId));
    }

    /**
     * Просмотр информации о конкретной вещи по её идентификатору. Эндпойнт GET /items/{itemId}.
     * Информацию о вещи может просмотреть любой пользователь.
     */
    @GetMapping("/{itemId}") // Эндпоинт получения вещи по ее id
    public ItemWithBookingDto getItemByIdWithBooking(@RequestHeader(value = "X-Sharer-User-Id",
            required = false) Long ownerId, @PathVariable Long itemId) {
        log.info("Просмотр вещи по Id={} с информацией о бронировании", itemId);
        return itemService.getItemByIdWithBooking(itemId, ownerId);
    }

    /**
     * Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт GET /items.
     */
    @GetMapping //Эндпоинт получения списка вещей владельца
    public List<ItemWithBookingDto> getListItems(
            @RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Просмотр вещей пользователя по Id={}", ownerId);
        return itemService.getListItemsUserById(ownerId, from, size);
    }

    /**
     * Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст, и система ищет вещи,
     * содержащие этот текст в названии или описании. Происходит по эндпойнту /items/search?text={text},
     * в text передаётся текст для поиска. Проверьте, что поиск возвращает только доступные для аренды вещи.
     */
    @GetMapping("/search") // Эндпоинт поиска по подстроке
    public List<ItemDto> getSearchItems(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getSearchItems(text, from, size);
    }

    /**
     * Теперь дайте пользователям возможность оставлять отзыв на вещь. Отзыв может
     * оставить только тот пользователь, который брал эту вещь в аренду, и только после
     * окончания срока аренды. Так комментарии будут честными. Добавление
     * комментария будет происходить по эндпоинту POST /items/{itemId}/comment
     */
    @PostMapping("/{itemId}/comment")// Эндпоинт добавления комментария
    public CommentDto addComment(@RequestHeader(value = "X-Sharer-User-Id", required = false)
                                 Long ownerId,
                                 @RequestBody Comment comment, @PathVariable Long itemId) {
        return toCommentDto(itemService.addComment(comment, ownerId, itemId));
    }
}
