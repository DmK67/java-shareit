package ru.practicum.shareit.item.comment.repositiry;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
