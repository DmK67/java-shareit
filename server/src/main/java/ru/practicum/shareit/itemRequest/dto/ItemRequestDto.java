package ru.practicum.shareit.itemRequest.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ItemRequestDto {
    //id — уникальный идентификатор запроса;
    //description — текст запроса, содержащий описание требуемой вещи;
    //requestor — пользователь, создавший запрос;
    //created — дата и время создания запроса.

    private long id;

    private String description;

    private User requestor;

    private LocalDateTime created;
}