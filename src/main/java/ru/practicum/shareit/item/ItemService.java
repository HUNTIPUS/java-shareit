package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(ItemDto itemDto, Long userId);

    Item update(ItemDto itemDto, Long userId);

    ItemDto getById(Long itemId, Long ownerId);

    List<ItemDto> getAll(Long userId);

    List<Item> getByText(String text);
}
