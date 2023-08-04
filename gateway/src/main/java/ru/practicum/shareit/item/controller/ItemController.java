package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    /**
     * Добавление новой вещи. Будет происходить по эндпойнту POST /items. На вход поступает объект ItemDto.
     * userId в заголовке X-Sharer-User-Id — это идентификатор пользователя, который добавляет вещь.
     * Именно этот пользователь — владелец вещи. Идентификатор владельца будет поступать на вход в каждом из запросов,
     * рассмотренных далее.
     */

    @PostMapping // Эндпоинт добавления вещи
    public ResponseEntity<Object> addItem(@Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id", required = false)
                                          Long ownerId, @RequestBody ItemDto itemDto) {
        log.info("Добавляем вещь: {}", itemDto.getName());
        return itemClient.addItem(itemDto, ownerId);
    }

    /**
     * Редактирование вещи. Эндпойнт PATCH /items/{itemId}. Изменить можно название, описание и статус доступа к аренде.
     * Редактировать вещь может только её владелец.
     */
    @PatchMapping("/{itemId}") // Эндпоинт обновления вещи по id
    public ResponseEntity<Object> updateItem(@Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id",
            required = false) Long ownerId, @Min(1) @NotNull @PathVariable Long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Обновляем вещь по Id={}", itemId);
        return itemClient.updateItem(itemDto, itemId, ownerId);
    }

    /**
     * Просмотр информации о конкретной вещи по её идентификатору. Эндпойнт GET /items/{itemId}.
     * Информацию о вещи может просмотреть любой пользователь.
     */
    @GetMapping("/{itemId}") // Эндпоинт получения вещи по ее id
    public ResponseEntity<Object> getItemByIdWithBooking(@Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id",
            required = false) Long ownerId, @Min(1) @NotNull @PathVariable Long itemId) {
        log.info("Просмотр вещи по Id={} с информацией о бронировании", itemId);
        return itemClient.getItemByIdWithBooking(itemId, ownerId);
    }

    /**
     * Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт GET /items.
     */
    @GetMapping //Эндпоинт получения списка вещей владельца
    public ResponseEntity<Object> getListItems(
            @Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Просмотр вещей пользователя по Id={}", ownerId);
        return itemClient.getListItemsUserById(ownerId, from, size);
    }

    /**
     * Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст, и система ищет вещи,
     * содержащие этот текст в названии или описании. Происходит по эндпойнту /items/search?text={text},
     * в text передаётся текст для поиска. Проверьте, что поиск возвращает только доступные для аренды вещи.
     */
    @GetMapping("/search") // Эндпоинт поиска по подстроке
    public ResponseEntity<Object> getSearchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(value = "text", required = false) String text,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        return itemClient.getSearchItems(userId, text, from, size);
    }

    /**
     * Теперь дайте пользователям возможность оставлять отзыв на вещь. Отзыв может
     * оставить только тот пользователь, который брал эту вещь в аренду, и только после
     * окончания срока аренды. Так комментарии будут честными. Добавление
     * комментария будет происходить по эндпоинту POST /items/{itemId}/comment
     */
    @PostMapping("/{itemId}/comment")// Эндпоинт добавления комментария
    public ResponseEntity<Object> addComment(@Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id",
            required = false)
                                             Long ownerId,
                                             @RequestBody CommentDto commentDto,
                                             @Min(1) @NotNull @PathVariable Long itemId) {
        return itemClient.addComment(itemId, ownerId, commentDto);
    }
}
