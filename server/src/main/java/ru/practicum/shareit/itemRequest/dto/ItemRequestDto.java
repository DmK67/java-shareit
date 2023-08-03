package ru.practicum.shareit.itemRequest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@Builder
public class ItemRequestDto {

    /**
     * id — уникальный идентификатор запроса;
     */
    private long id;
    /**
     * description — текст запроса, содержащий описание требуемой вещи;
     */
    @NotBlank
    @NotNull
    private String description;
    /**
     * requestor — пользователь, создавший запрос;
     */
    private User requestor;
    /**
     * created — дата и время создания запроса.
     */
    private LocalDateTime created;
}
