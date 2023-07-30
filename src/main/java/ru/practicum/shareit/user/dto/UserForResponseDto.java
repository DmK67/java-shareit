package ru.practicum.shareit.user.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Validated
@Builder
public class UserForResponseDto {
    private Long id;
    @NotBlank
    @NotNull
    private String name;
}
