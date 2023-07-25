package ru.practicum.shareit.item.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

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

    public static List<CommentDto> convertListCommentsToListCommentsDto(List<Comment> listComments) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        if (listComments == null) {
            listComments = new ArrayList<>();
        }
        for (Comment comment : listComments) {
            commentDtoList.add(toCommentDto(comment));
        }
        return commentDtoList;
    }
}
