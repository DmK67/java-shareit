package ru.practicum.shareit.itemRequest.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemForResponseDto;
import ru.practicum.shareit.user.dto.UserForResponseDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemRequestDtoWithAnswers {
    Long id;                //уникальный идентификатор запроса;
    @NotBlank
    String description;     //текст запроса, содержащий описание требуемой вещи;
    UserForResponseDto requester;         //пользователь, создавший запрос;
    LocalDateTime created;  //дата и время создания запроса.
    List<ItemForResponseDto> items;

}
