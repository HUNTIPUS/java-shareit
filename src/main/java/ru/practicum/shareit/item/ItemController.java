package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comments.CommentService;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.Create;
import ru.practicum.shareit.user.Update;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto create(@RequestBody @Validated(Create.class) ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemMapper.toItemDto(itemService
                .create(itemDto, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody @Validated(Update.class) ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable("itemId") Long itemId) {
        itemDto.setId(itemId);
        return ItemMapper.toItemDto(itemService
                .update(itemDto, userId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.getById(itemId, ownerId);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByText(@RequestParam("text") String text) {
        if (text.isBlank()) {
            return List.of();
        } else {
            return ItemMapper.toListItemDto(itemService.getByText(text.toLowerCase()));
        }
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto,
                                    @PathVariable("itemId") Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return commentService.create(commentDto, itemId, userId);
    }
}
