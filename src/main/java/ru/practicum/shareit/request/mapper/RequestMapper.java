package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public static ItemRequestDtoOutput toRequestDto(ItemRequest request) {
        return ItemRequestDtoOutput.builder()
                .id(request.getId())
                .description(request.getDescriptionRequest())
                .created(request.getCreated())
                .build();
    }

    public static ItemRequest toRequest(ItemRequestDtoInput requestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescriptionRequest(requestDto.getDescription());
        itemRequest.setCreated(requestDto.getCreated());
        return itemRequest;
    }

    public static List<ItemRequestDtoOutput> toRequestDtoList(List<ItemRequest> requests) {
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }
}
