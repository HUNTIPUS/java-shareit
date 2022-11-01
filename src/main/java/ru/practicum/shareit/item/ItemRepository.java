package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(Item item);

    Item updateItem(Item item);

    Optional<Item> getItemById(Long itemId);

    List<Item> getItems(Long userId);

    List<Item> getItemsByText(String text);
}
