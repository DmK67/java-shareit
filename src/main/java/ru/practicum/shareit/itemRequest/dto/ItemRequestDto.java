package ru.practicum.shareit.itemRequest.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
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
