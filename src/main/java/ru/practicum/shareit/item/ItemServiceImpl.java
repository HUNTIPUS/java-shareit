package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.comments.CommentMapper;
import ru.practicum.shareit.comments.CommentRepository;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.exeption.ObjectExcistenceException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public Item create(ItemDto itemDto, Long userId) {
        userService.getById(userId);
        return itemRepository.save(ItemMapper.toItem(itemDto, userId));
    }

    @Override
    @Transactional
    public Item update(ItemDto itemDto, Long userId) {
        if (getById(itemDto.getId(), userId).getOwnerId().equals(userId)) {
            return itemRepository.save(ItemMapper.toItem(updateItemIfParamIsNull(itemDto), userId));
        } else {
            throw new ObjectExcistenceException("У этого инструмента другой владелец");
        }
    }

    @Override
    public ItemDto getById(Long itemId, Long ownerId) {
        Item item = itemRepository.getById(itemId);
        if (item == null) {
            throw new ObjectExcistenceException("Инструмент не сущестует");
        }
        ItemDto itemDto = ItemMapper.toItemDto(item);
        List<Booking> bookings = getBookingByItemId(itemId, ownerId);
        if (bookings.size() == 2) {
            itemDto.setLastBooking(bookings.get(1));
            itemDto.setNextBooking(bookings.get(0));
        } else if (bookings.size() == 1) {
            itemDto.setLastBooking(bookings.get(0));
        }
        itemDto.setComments(getByItemId(itemId));
        return itemDto;
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        List<Item> items = itemRepository.getAll(userId);
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : items) {
            itemDtoList.add(getById(item.getId(), item.getOwnerId()));
        }
        return itemDtoList;
    }

    @Override
    public List<Item> getByText(String text) {
        return itemRepository.getByText(text);

    }

    private ItemDto updateItemIfParamIsNull(ItemDto item) {
        ItemDto itemNew = getById(item.getId(), item.getOwnerId());
        if (item.getName() != null && !item.getName().isBlank()) {
            itemNew.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            itemNew.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemNew.setAvailable(item.getAvailable());
        }
        if (item.getRequest() != null) {
            itemNew.setRequest(item.getRequest());
        }
        return itemNew;
    }

    private List<Booking> getBookingByItemId(Long itemId, Long ownerId) {
        return bookingRepository.getBookingByItemId(itemId, ownerId)
                .stream().limit(2).collect(Collectors.toList());
    }

    private List<CommentDto> getByItemId(Long itemId) {
        List<Comment> comments = commentRepository.getAll(itemId);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtoList.add(mapperToComment(comment));
        }
        return commentDtoList;
    }

    private CommentDto mapperToComment(Comment comment) {
        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        Item item = itemRepository.getById(commentDto.getItemId());
        User user = userService.getById(commentDto.getAuthorId());
        commentDto.setItem(item);
        commentDto.setItemId(item.getId());
        commentDto.setAuthor(user);
        commentDto.setAuthorId(user.getId());
        commentDto.setAuthorName(user.getName());
        return commentDto;
    }
}
