package ru.practicum.shareit.user.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;


@Getter
@Setter
@AllArgsConstructor
@Validated
@Builder
@EqualsAndHashCode
public class UserForResponseDto {
    private Long id;
    private String name;
}
