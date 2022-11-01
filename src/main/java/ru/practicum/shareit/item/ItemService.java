package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ObjectExcistenceException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    public Item createItem(Item item, Long userId) {
        item.setOwner(userService.getUserById(userId));
        return itemRepository.createItem(item);
    }

    public Item updateItem(ItemDto itemDto, Long userId, Long itemId) {
        userService.getUserById(userId);
        getItemById(itemId);
        itemDto.setId(itemId);
        return itemRepository.updateItem(ItemMapper.toItem(itemDto, userService.getUserById(userId)));
    }

    public Item getItemById(Long itemId) {
        return itemRepository.getItemById(itemId)
                .orElseThrow(() -> new ObjectExcistenceException("Вещи c таким id не существует"));
    }

    public List<Item> getItems(Long userId) {
        return itemRepository.getItems(userId);
    }

    public List<Item> getItemsByText(String text) {
        return itemRepository.getItemsByText(text);
    }
}
