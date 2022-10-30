package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public Item createItem(@RequestBody @Valid ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") Integer userId) {
        userService.getUserById(userId);
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestBody ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable("itemId") Integer itemId) {
        userService.getUserById(userId);
        itemService.getItemById(itemId);
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("{itemId}")
    public Item getItemById(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<Item> getItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public List<Item> getItemsByText(@RequestParam("text") String text) {
        return itemService.getItemsByText(text.toLowerCase());
    }
}
