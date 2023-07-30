package ru.practicum.shareit.user.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

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

    @Email()
    private String email;
}
