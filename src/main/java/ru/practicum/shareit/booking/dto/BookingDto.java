package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.Create;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingDto {

    private Long id;
    @FutureOrPresent(groups = {Create.class})
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Item item;
    private Long bookerId;
    private User booker;
    private Status status = Status.WAITING;


    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, Long itemId, Long bookerId, Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.bookerId = bookerId;
        this.status = status;
    }
}
