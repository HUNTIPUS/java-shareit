package ru.practicum.shareit.booking.service.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.dal.BookingService;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.dal.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.dal.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.data.domain.Sort.Direction.DESC;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private BookingService bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    private User booker;
    private User owner;
    private Item item;
    private Booking booking;
    private BookingDtoInput bookingDtoInput;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, userService, itemService);
        booker = new User();
        booker.setId(1L);
        booker.setName("Чича");
        booker.setEmail("koti@yandex.ru");

        owner = new User();
        owner.setId(2L);
        owner.setName("Юля");
        owner.setEmail("jul@yandex.ru");

        item = new Item();
        item.setId(1L);
        item.setOwner(owner);
        item.setName("Мячик");
        item.setAvailable(true);
        item.setDescription("Теннисный мячик");

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2022, 12, 8, 8, 0));
        booking.setEnd(LocalDateTime.of(2022, 12, 10, 8, 0));
        booking.setStatus(Status.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);

        bookingDtoInput = new BookingDtoInput();
        bookingDtoInput.setId(booking.getId());
        bookingDtoInput.setStart(booking.getStart());
        bookingDtoInput.setEnd(booking.getEnd());
        bookingDtoInput.setItemId(item.getId());
    }

    @Test
    void createTest() {

        Mockito
                .when(userService.getById(anyLong()))
                .thenReturn(booker);
        Mockito
                .when(itemService.getByIdForItem(item.getId()))
                .thenReturn(item);
        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        BookingDtoOutput bookingDtoOutput = bookingService.create(bookingDtoInput, booker.getId());

        Assertions.assertEquals(booking.getId(), bookingDtoOutput.getId());
        Assertions.assertEquals(booking.getStart(), bookingDtoOutput.getStart());
        Assertions.assertEquals(booking.getEnd(), bookingDtoOutput.getEnd());
        Assertions.assertEquals(booking.getItem().getId(), bookingDtoOutput.getItem().getId());
        Assertions.assertEquals(booking.getItem().getName(), bookingDtoOutput.getItem().getName());
        Assertions.assertEquals(booking.getStatus(), bookingDtoOutput.getStatus());
        Assertions.assertEquals(booking.getBooker().getId(), bookingDtoOutput.getBooker().getId());


    }

    @Test
    void updateTest() {
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        BookingDtoOutput bookingDtoOutput = bookingService.update(booking.getId(), owner.getId(), true);
        Assertions.assertEquals(booking.getId(), bookingDtoOutput.getId());
        Assertions.assertEquals(booking.getStart(), bookingDtoOutput.getStart());
        Assertions.assertEquals(booking.getEnd(), bookingDtoOutput.getEnd());
        Assertions.assertEquals(booking.getItem().getId(), bookingDtoOutput.getItem().getId());
        Assertions.assertEquals(booking.getItem().getName(), bookingDtoOutput.getItem().getName());
        Assertions.assertEquals(booking.getStatus(), bookingDtoOutput.getStatus());
        Assertions.assertEquals(booking.getBooker().getId(), bookingDtoOutput.getBooker().getId());
    }

    @Test
    void getByIdTest() {
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        BookingDtoOutput bookingDtoOutput = bookingService.getById(booking.getId(), booker.getId());
        Assertions.assertEquals(booking.getId(), bookingDtoOutput.getId());
        Assertions.assertEquals(booking.getStart(), bookingDtoOutput.getStart());
        Assertions.assertEquals(booking.getEnd(), bookingDtoOutput.getEnd());
        Assertions.assertEquals(booking.getItem().getId(), bookingDtoOutput.getItem().getId());
        Assertions.assertEquals(booking.getItem().getName(), bookingDtoOutput.getItem().getName());
        Assertions.assertEquals(booking.getStatus(), bookingDtoOutput.getStatus());
        Assertions.assertEquals(booking.getBooker().getId(), bookingDtoOutput.getBooker().getId());
    }

    @Test
    void getAllByOwnerTest() {
        List<BookingDtoOutput> bookingDtoOutputList = new ArrayList<>();
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        BookingDtoOutput bookingDtoOutput = bookingService.getById(booking.getId(), booker.getId());
        bookingDtoOutputList.add(bookingDtoOutput);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        Mockito
                .when(bookingRepository.getAllByOwnerAll(owner.getId(),
                        PageRequest.of(0, 1, Sort.by(DESC, "start"))))
                .thenReturn(bookings);
        List<BookingDtoOutput> newBookingDtoOutputList =
                bookingService.getAllByOwner(owner.getId(), "ALL", 0, 1);

        Assertions.assertEquals(bookingDtoOutputList.get(0).getId(),
                newBookingDtoOutputList.get(0).getId());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getStart(),
                newBookingDtoOutputList.get(0).getStart());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getEnd(),
                newBookingDtoOutputList.get(0).getEnd());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getItem().getId(),
                newBookingDtoOutputList.get(0).getItem().getId());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getItem().getName(),
                newBookingDtoOutputList.get(0).getItem().getName());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getStatus(),
                newBookingDtoOutputList.get(0).getStatus());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getBooker().getId(),
                newBookingDtoOutputList.get(0).getBooker().getId());

    }

    @Test
    void getAllByBookerTest() {
        List<BookingDtoOutput> bookingDtoOutputList = new ArrayList<>();
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        BookingDtoOutput bookingDtoOutput = bookingService.getById(booking.getId(), booker.getId());
        bookingDtoOutputList.add(bookingDtoOutput);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        Mockito
                .when(bookingRepository.getAllByBookerAll(booker.getId(),
                        PageRequest.of(0, 1, Sort.by(DESC, "start"))))
                .thenReturn(bookings);
        List<BookingDtoOutput> newBookingDtoOutputList =
                bookingService.getAllByBooker(booker.getId(), "ALL", 0, 1);

        Assertions.assertEquals(bookingDtoOutputList.get(0).getId(),
                newBookingDtoOutputList.get(0).getId());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getStart(),
                newBookingDtoOutputList.get(0).getStart());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getEnd(),
                newBookingDtoOutputList.get(0).getEnd());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getItem().getId(),
                newBookingDtoOutputList.get(0).getItem().getId());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getItem().getName(),
                newBookingDtoOutputList.get(0).getItem().getName());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getStatus(),
                newBookingDtoOutputList.get(0).getStatus());
        Assertions.assertEquals(bookingDtoOutputList.get(0).getBooker().getId(),
                newBookingDtoOutputList.get(0).getBooker().getId());

    }

    @Test
    void getByIdForBookingTest() {
        Mockito
                .when(bookingRepository.findById(booking.getId()))
                .thenReturn(Optional.of(booking));

        Booking newBooking = bookingService.getByIdForBooking(booking.getId());
        Assertions.assertEquals(booking.getId(), newBooking.getId());
        Assertions.assertEquals(booking.getStart(), newBooking.getStart());
        Assertions.assertEquals(booking.getEnd(), newBooking.getEnd());
        Assertions.assertEquals(booking.getItem(), newBooking.getItem());
        Assertions.assertEquals(booking.getStatus(), newBooking.getStatus());
        Assertions.assertEquals(booking.getBooker(), newBooking.getBooker());
    }
}