package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private final UserService userService;

    private final ValidationService validationService;

    //Добавление новой вещи. Будет происходить по эндпойнту POST /items. На вход поступает объект ItemDto.
    // userId в заголовке X-Sharer-User-Id — это идентификатор пользователя, который добавляет вещь.
    // Именно этот пользователь — владелец вещи. Идентификатор владельца будет поступать на вход в каждом из запросов,
    // рассмотренных далее.
    @PostMapping
    public ItemDto addItem(@Valid @Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                           @Valid @RequestBody ItemDto itemDto) {
        userService.getUserById(ownerId);
        log.info("Добавляем вещь: " + itemDto.getName());
        return toItemDto(itemService.addItem(toItem(itemDto), ownerId));
    }

    //Редактирование вещи. Эндпойнт PATCH /items/{itemId}. Изменить можно название, описание и статус доступа к аренде.
    // Редактировать вещь может только её владелец.
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Valid @Min(1) @NotNull @RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                              @Valid @Min(1) @NotNull @PathVariable Long itemId, @Validated @RequestBody ItemDto itemDto) {
        itemService.getItemById(itemId);
        log.info("Обновляем вещь: " + itemDto.getName());
        //Item item = toItem(itemDto);

        return null;
    }

    //Просмотр информации о конкретной вещи по её идентификатору. Эндпойнт GET /items/{itemId}.
    //Информацию о вещи может просмотреть любой пользователь.
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                               @PathVariable Long itemId) {
        return null;
    }

    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт GET /items.
    @GetMapping
    public List<ItemDto> getListItems(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId) {
        return null;
    }

    //Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст, и система ищет вещи,
    // содержащие этот текст в названии или описании. Происходит по эндпойнту /items/search?text={text},
    // в text передаётся текст для поиска. Проверьте, что поиск возвращает только доступные для аренды вещи.
    @GetMapping("/search")
    public List<ItemDto> getSearchItems(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                                        @RequestParam(value = "text", required = false) String text) {
        return null;
    }

}
