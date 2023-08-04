package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Builder
public class CommentDto {
    private Long id;
    private String text;

    private Optional item;

    private String authorName;

    private LocalDateTime created;

}
