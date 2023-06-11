package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.Min;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@Validated
public class Item {
    //id — уникальный идентификатор вещи;
    //name — краткое название;
    //description — развёрнутое описание;
    //available — статус о том, доступна или нет вещь для аренды;
    //owner — владелец вещи;
    //request — если вещь была создана по запросу другого пользователя, то в этом
    //поле будет храниться ссылка на соответствующий запрос.


    private Long id;
    private String name;
    private String description;
    private boolean available;
    private Long owner;
    private ItemRequest request;

    public Item(Long id, String name, String description, boolean available, Long owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}

