package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto create(BookingDto bookingDto, Long userId);

    BookingDto update(Long bookingId, Long userId, Boolean approved);

    BookingDto getById(Long bookingId, Long userId);

    List<BookingDto> getAllByBooker(Long bookerId, String state) throws IllegalAccessException;

    List<BookingDto> getAllByOwner(Long ownerId, String state) throws IllegalAccessException;
}
