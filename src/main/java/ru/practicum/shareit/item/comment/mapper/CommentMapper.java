package ru.practicum.shareit.item.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

@Component
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) { // Метод перевода объекта item в объект itemDto
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(CommentDto commentDto) { // Метод перевода объекта itemDto в объект item
        User user = User.builder()
                .name(commentDto.getAuthorName())
                .build();
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .author(user)
                .created(commentDto.getCreated())
                .build();
    }
}
