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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
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
            throw new ObjectExcistenceException("?? ?????????? ?????????????????????? ???????????? ????????????????");
        }
    }

    @Override
    public ItemDtoOutput getById(Long itemId, Long ownerId) {
        List<Item> items = new ArrayList<>();
        items.add(getByIdForItem(itemId));
        Map<Item, List<Booking>> approvedBookings = getApprovedBookings(items);
        Map<Item, List<Comment>> comments = getComments(items);
        if (items.get(0).getOwner().getId().equals(ownerId)) {
            return appendCommentsToItem(appendBookingToItem(items.get(0),
                            approvedBookings.getOrDefault(items.get(0), Collections.emptyList())),
                    comments.getOrDefault(items.get(0), Collections.emptyList()));
        }
        return appendCommentsToItem(ItemMapper.toItemDto(items.get(0)),
                comments.getOrDefault(items.get(0), Collections.emptyList()));
    }

    @Override
    public List<ItemDtoOutput> getAll(Long userId) {
        List<Item> items = itemRepository.getAll(userId);
        Map<Item, List<Booking>> approvedBookings = getApprovedBookings(items);
        Map<Item, List<Comment>> comments = getComments(items);
        List<ItemDtoOutput> itemDtoOutputList = new ArrayList<>();
        for (Item item : items) {
            itemDtoOutputList.add(appendCommentsToItem(appendBookingToItem(item,
                            approvedBookings.getOrDefault(item, Collections.emptyList())),
                    comments.getOrDefault(item, Collections.emptyList())));
        }
        return itemDtoOutputList;
    }

    @Override
    public List<ItemDtoOutput> getByText(String text) {
        return ItemMapper.toItemDtoList(itemRepository.getByText(text));
    }

    private Map<Item, List<Booking>> getApprovedBookings(List<Item> items) {
        return bookingRepository.findApprovedForItems(
                        items, Sort.by(DESC, "start"))
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));
    }

    public ItemDtoOutput appendBookingToItem(Item item, List<Booking> bookings) {
        ItemDtoOutput itemDto = ItemMapper.toItemDto(item);
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = bookings.stream()
                .filter(b -> ((b.getEnd().isEqual(now) || b.getEnd().isBefore(now))
                        || (b.getStart().isEqual(now) || b.getStart().isBefore(now))))
                .findFirst()
                .orElse(null);
        Booking nextBooking = bookings.stream()
                .filter(b -> b.getStart().isAfter(now))
                .reduce((first, second) -> second)
                .orElse(null);
        ItemDtoOutput.Booking lastBookingNew = new ItemDtoOutput.Booking();
        ItemDtoOutput.Booking nextBookingNew = new ItemDtoOutput.Booking();
        if (lastBooking != null) {
            lastBookingNew.setId(lastBooking.getId());
            lastBookingNew.setBookerId(lastBooking.getBooker().getId());
            itemDto.setLastBooking(lastBookingNew);
        }
        if (nextBooking != null) {
            nextBookingNew.setId(nextBooking.getId());
            nextBookingNew.setBookerId(nextBooking.getBooker().getId());
            itemDto.setNextBooking(nextBookingNew);
        }

        return itemDto;
    }

    public ItemDtoOutput appendCommentsToItem(ItemDtoOutput itemDto, List<Comment> comments) {
        itemDto.setComments(CommentMapper.toListItemCommentDto(comments));
        return itemDto;
    }

    private Map<Item, List<Comment>> getComments(List<Item> items) {
        return commentRepository.findCommentForItems(items)
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));
    }

    @Override
    public Item getByIdForItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectExcistenceException("???????????????????? ???? ??????????????????"));
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
}
