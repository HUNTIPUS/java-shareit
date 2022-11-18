package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.Create;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @Validated(Create.class) @RequestBody BookingDto bookingDto) {

        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@RequestParam("approved") Boolean approved,
                             @PathVariable("bookingId") Long bookingId,
                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable("bookingId") Long bookingId,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) throws IllegalAccessException {
        return bookingService.getAllByOwner(userId, state);
    }

    @GetMapping()
    public List<BookingDto> getAllByBooker(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) throws IllegalAccessException {
        return bookingService.getAllByBooker(userId, state);
    }

}
