package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                booking.getBookerId(),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        if (bookingDto.getId() != null) {
            booking.setId(bookingDto.getId());
        }
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItemId(bookingDto.getItemId());
        booking.setBookerId(bookingDto.getBooker().getId());
        booking.setStatus(bookingDto.getStatus());

        return booking;
    }
}
