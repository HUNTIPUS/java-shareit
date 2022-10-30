package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ObjectExcistenceException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item createItem(ItemDto itemDto, Integer userId) {
        return itemRepository.createItem(itemDto, userId);
    }

    public Item updateItem(ItemDto itemDto, Integer userId, Integer itemId){
        itemDto.setId(itemId);
        return itemRepository.updateItem(itemDto, userId);
    }

    public Item getItemById(Integer itemId) {
        return itemRepository.getItemById(itemId)
                .orElseThrow(() -> new ObjectExcistenceException("Вещи c таким id не существует"));
    }

    public List<Item> getItems(Integer userId) {
        return itemRepository.getItems(userId);
    }

    public List<Item> getItemsByText(String text) {
        return itemRepository.getItemsByText(text);
    }
}
