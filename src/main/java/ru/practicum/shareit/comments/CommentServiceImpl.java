package ru.practicum.shareit.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    @Transactional
    public CommentDto create(CommentDto commentDto, Long itemId, Long userId) {
        ItemDto itemDto = itemService.getById(itemId, itemService.getById(itemId, userId).getOwnerId());
        if (itemDto.getLastBooking().getBookerId().equals(userId)
                && !itemDto.getLastBooking().getStatus().equals(Status.REJECTED)
                && !commentDto.getText().isBlank()) {
            commentDto.setItemId(itemId);
            commentDto.setAuthorId(userId);
            Comment comment = commentRepository.save(CommentMapper.toComment(commentDto));
            CommentDto commentDtoNew = CommentMapper.toCommentDto(comment);
            commentDtoNew.setItem(ItemMapper.toItem(itemDto, userId));
            commentDtoNew.setAuthor(userService.getById(userId));
            commentDtoNew.setAuthorName(commentDtoNew.getAuthor().getName());
            return commentDtoNew;
        } else {
            throw new ValidationException("Предмет не бронировался или текст отзыва пустой");
        }
    }
}
