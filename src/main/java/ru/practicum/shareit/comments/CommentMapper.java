package ru.practicum.shareit.comments;

import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        if (commentDto.getId() != null) {
            comment.setId(comment.getId());
        }
        comment.setText(commentDto.getText());
        comment.setItemId(commentDto.getItemId());
        comment.setAuthorId(commentDto.getAuthorId());
        comment.setCreated(commentDto.getCreated());
        return comment;
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getItemId(),
                comment.getAuthorId(),
                comment.getCreated());
    }

    public static List<CommentDto> toListUserDto(List<Comment> comments) {
        return comments
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
