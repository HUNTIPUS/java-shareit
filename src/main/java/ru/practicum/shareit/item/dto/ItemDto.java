package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.user.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemDto {

    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private Long ownerId;
    private Long request;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentDto> comments;

    public ItemDto(Long id, String name, String description, Boolean available, Long ownerId, Long request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
        this.request = request;
    }
}
