package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    //id — уникальный идентификатор вещи;
    //name — краткое название;
    //description — развёрнутое описание;
    //available — статус о том, доступна или нет вещь для аренды;
    //owner — владелец вещи;
    //request — если вещь была создана по запросу другого пользователя, то в этом
    //поле будет храниться ссылка на соответствующий запрос.
    private long id;
    private String name;
    private String description;
    private Booking available;
    private User owner;
    private ItemRequest request;
}
