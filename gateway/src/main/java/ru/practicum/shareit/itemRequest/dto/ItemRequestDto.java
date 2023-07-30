package ru.practicum.shareit.itemRequest.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

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
    @NotBlank
    @NotNull
    private String description;

    private Optional requestor;

    private LocalDateTime created;
}
