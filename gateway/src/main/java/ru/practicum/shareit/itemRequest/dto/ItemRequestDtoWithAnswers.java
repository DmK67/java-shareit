package ru.practicum.shareit.itemRequest.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemForResponseDto;
import ru.practicum.shareit.user.dto.UserForResponseDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ItemRequestDtoWithAnswers {
    Long id;                //уникальный идентификатор запроса;
    @NotBlank
    @NotNull
    private String description;     //текст запроса, содержащий описание требуемой вещи;
    private UserForResponseDto requester;         //пользователь, создавший запрос;
    private LocalDateTime created;  //дата и время создания запроса.
    private List<ItemForResponseDto> items;

}
