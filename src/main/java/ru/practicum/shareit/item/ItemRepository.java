package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(ItemDto itemDto, Integer userId);
    Item updateItem(ItemDto itemDto, Integer userId);
    Optional<Item> getItemById(Integer itemId);
    List<Item> getItems(Integer userId);
    List<Item> getItemsByText(String text);
}
