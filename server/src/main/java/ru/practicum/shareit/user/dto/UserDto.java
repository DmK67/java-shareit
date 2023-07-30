package ru.practicum.shareit.user.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
@EqualsAndHashCode
public class UserDto {

    private Long id;
    private String name;
    private String email;
}
