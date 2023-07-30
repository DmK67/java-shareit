package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ItemDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;

}
