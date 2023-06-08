package ru.practicum.shareit.item.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Validated
//@AllArgsConstructor
public class ItemDto {

    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @AssertTrue
    private Boolean available;
    private long request;
    private long ownerId;

    public ItemDto(long id, String name, String description, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
