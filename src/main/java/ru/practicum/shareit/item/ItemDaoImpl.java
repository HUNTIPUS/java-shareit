package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.ObjectExcistenceException;
import ru.practicum.shareit.generate.GenerateId;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemDaoImpl implements ItemRepository {

    private Map<Integer, Item> items = new HashMap<>();
    private GenerateId generateId = new GenerateId();

    @Override
    public Item createItem(ItemDto itemDto, Integer userId) {
        itemDto.setId(generateId.getId());
        Item item = ItemMapper.toItem(itemDto, userId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(ItemDto itemDto, Integer userId) {
        Item item = ItemMapper.toItem(itemDto, userId);
        if(!items.values().stream().filter(x -> x.getOwner().equals(item.getOwner())
                && x.getId().equals(item.getId())).findFirst().isEmpty()){
            Item newItem = items.get(item.getId());
            return doUpdateItem(item, newItem);
        } else {
            throw new ObjectExcistenceException("Ошибка в индексах");
        }
    }

    @Override
    public Optional<Item> getItemById(Integer itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getItems(Integer userId) {
        return items.values().stream().filter(x -> x.getOwner().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsByText(String text) {
        if (!text.isBlank()) {
            return items.values().stream().filter(x -> (x.getDescription().toLowerCase().contains(text)
                            || x.getName().toLowerCase().contains(text)) && x.getAvailable().equals(true))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private Item doUpdateItem(Item item, Item newItem) {
        if (item.getName() != null) {
            newItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }
        if (item.getRequest() != null) {
            newItem.setRequest(item.getRequest());
        }
        return newItem;
    }
}
