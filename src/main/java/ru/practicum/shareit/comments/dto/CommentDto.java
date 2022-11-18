package ru.practicum.shareit.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String text;
    private Long itemId;
    private Item item;
    private Long authorId;
    private User author;
    private String authorName;
    private LocalDateTime created = LocalDateTime.now();

    public CommentDto(Long id, String text, Long itemId, Long authorId, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.itemId = itemId;
        this.authorId = authorId;
        this.created = created;
    }
}
