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

    private long id;
    @NotBlank
    @NotNull
    private String description;

    private Optional requestor;

    private LocalDateTime created;
}
