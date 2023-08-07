package ru.practicum.shareit.item.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private Item item;

    private String authorName;

    private LocalDateTime created;

}
