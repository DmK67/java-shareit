package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@Validated
@Builder
public class UserDto {

    private Long id;

    private String name;

    @Email()
    private String email;
}
