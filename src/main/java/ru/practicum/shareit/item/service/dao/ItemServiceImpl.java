package ru.practicum.shareit.item.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comments.mapper.CommentMapper;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.comments.repository.CommentRepository;
import ru.practicum.shareit.exeption.exeptions.ObjectExcistenceException;
import ru.practicum.shareit.item.dto.ItemDtoInput;
import ru.practicum.shareit.item.dto.ItemDtoOutput;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.dal.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.dal.UserService;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
    public ItemDtoOutput create(ItemDtoInput itemDto, Long userId) {
        User user = userService.getById(userId);
        Item item = itemRepository.save(ItemMapper.toItem(itemDto));
        item.setOwner(user);
        return ItemMapper.toItemDto(item);
    }

    @Override
    @Transactional
    public ItemDtoOutput update(ItemDtoInput itemDto, Long userId) {
        Item item = getByIdForItem(itemDto.getId());
        Item itemNew = ItemMapper.toItem(itemDto);
        if (item.getOwner().getId().equals(userId)) {
            itemNew = updateItemIfParamIsNull(itemNew);
            return getById(itemNew.getId(), itemNew.getOwner().getId());

        } else {
            throw new ObjectExcistenceException("У этого инструмента другой владелец");
        }
    }

    @Override
    public ItemDtoOutput getById(Long itemId, Long ownerId) {
        Item item = getByIdForItem(itemId);
        ItemDtoOutput itemDto =
                appendBookingToItem(ItemMapper.toItemDto(item), getBookingByItemId(itemId, ownerId));
        return appendCommentsToItem(itemDto, getByItemId(itemId));
    }

    @Override
    public List<ItemDtoOutput> getAll(Long userId) {
        List<ItemDtoOutput> itemDtoList = ItemMapper.toItemDtoList(itemRepository.getAll(userId));
        List<Booking> bookings = bookingRepository.findAll(Sort.by(DESC, "start"));
        for (ItemDtoOutput itemDto: itemDtoList) {
            List<Booking> bookingsNew = bookings.stream()
                    .filter(b -> b.getItem().getId().equals(itemDto.getId()))
                    .collect(toList());
            appendBookingToItem(itemDto, bookingsNew);
        }
        return itemDtoList;
    }

    @Override
    public List<ItemDtoOutput> getByText(String text) {
        return ItemMapper.toItemDtoList(itemRepository.getByText(text));
    }

    private ItemDtoOutput appendBookingToItem(ItemDtoOutput itemDto, List<Booking> bookings) {
        ItemDtoOutput.Booking lastBooking = new ItemDtoOutput.Booking();
        ItemDtoOutput.Booking nextBooking = new ItemDtoOutput.Booking();
        if (bookings.size() == 2) {
            lastBooking.setId(bookings.get(1).getId());
            lastBooking.setBookerId(bookings.get(1).getBooker().getId());
            itemDto.setLastBooking(lastBooking);
            nextBooking.setId(bookings.get(0).getId());
            nextBooking.setBookerId(bookings.get(0).getBooker().getId());
            itemDto.setNextBooking(nextBooking);
        } else if (bookings.size() == 1) {
            lastBooking.setId(bookings.get(0).getId());
            lastBooking.setBookerId(bookings.get(0).getBooker().getId());
            itemDto.setLastBooking(lastBooking);
        }
        return itemDto;
    }

    private ItemDtoOutput appendCommentsToItem(ItemDtoOutput itemDto, List<Comment> comments) {
        itemDto.setComments(CommentMapper.toListItemCommentDto(comments));
        return itemDto;
    }

    @Override
    public Item getByIdForItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectExcistenceException("Инструмент не сущестует"));
    }

    private Item updateItemIfParamIsNull(Item item) {
        Item itemNew = getByIdForItem(item.getId());
        if (item.getName() != null && !item.getName().isBlank()) {
            itemNew.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            itemNew.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemNew.setAvailable(item.getAvailable());
        }
        if (item.getRequestId() != null) {
            itemNew.setRequestId(item.getRequestId());
        }
        return itemNew;
    }

    private List<Booking> getBookingByItemId(Long itemId, Long ownerId) {
        return bookingRepository.getBookingByItemId(itemId, ownerId, Sort.by(DESC, "start"))
                .stream().limit(2).collect(toList());
    }

    private List<Comment> getByItemId(Long itemId) {
        return commentRepository.getAll(itemId);
    }
}
