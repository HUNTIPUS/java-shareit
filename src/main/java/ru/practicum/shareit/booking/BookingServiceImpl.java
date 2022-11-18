package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exeption.ObjectExcistenceException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public BookingDto create(BookingDto bookingDto, Long userId) {
        bookingDto.setBooker(userService.getById(userId));
        bookingDto.setItem(ItemMapper.toItem(itemService.getById(bookingDto.getItemId(), userId),
                itemService.getById(bookingDto.getItemId(), userId).getOwnerId()));
        if (!bookingDto.getItem().getOwnerId().equals(userId)) {
            Booking booking = BookingMapper.toBooking(bookingDto);
            if (itemService.getById(booking.getItemId(), userId).getAvailable()
                    && !booking.getStart().isAfter(booking.getEnd())) {
                return getById(bookingRepository.save(booking).getId(), userId);
            } else {
                throw new ValidationException("Вещь не доступна для аренды");
            }
        } else {
            throw new ObjectExcistenceException("Пользователь является владельцем");
        }
    }

    @Override
    @Transactional
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        BookingDto bookingDto = getById(bookingId, userId);
        if (!userId.equals(bookingDto.getBooker().getId())) {
            if (approved && !bookingDto.getStatus().equals(Status.APPROVED)) {
                bookingDto.setStatus(Status.APPROVED);
            } else if (!approved && !bookingDto.getStatus().equals(Status.REJECTED)) {
                bookingDto.setStatus(Status.REJECTED);
            } else {
                throw new ValidationException("Сделка имеет такой статус");
            }
            bookingDto.setBooker(userService.getById(bookingDto.getBooker().getId()));
            bookingDto.setItem(ItemMapper.toItem(itemService.getById(bookingDto.getItemId(), userId),
                    itemService.getById(bookingDto.getItemId(), userId).getOwnerId()));
            bookingRepository.save(BookingMapper.toBooking(bookingDto));
            return bookingDto;
        } else {
            throw new ObjectExcistenceException("Попытка изменения статуса сделки не владельцем");
        }
    }

    @Override
    public BookingDto getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.getById(bookingId, userId);
        if (booking == null) {
            throw new ObjectExcistenceException("Инструмент не сущестует");
        }
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        bookingDto.setItem(ItemMapper.toItem(itemService.getById(bookingDto.getItemId(), userId),
                itemService.getById(bookingDto.getItemId(), userId).getOwnerId()));
        bookingDto.setBooker(userService.getById(booking.getBookerId()));
        return bookingDto;
    }

    @Override
    public List<BookingDto> getAllByOwner(Long id, String state) throws IllegalAccessException {
        userService.getById(id);
        List<Booking> bookings;
        List<BookingDto> bookingDtoList = new ArrayList<>();
        if (state.equals(State.ALL.name())) {
            bookings = bookingRepository.getAllByOwnerAll(id);
        } else if (state.equals(State.CURRENT.name())) {
            bookings = bookingRepository.getAllByOwnerCurrent(id, LocalDateTime.now());
        } else if (state.equals(State.PAST.name())) {
            bookings = bookingRepository.getAllByOwnerPast(id, LocalDateTime.now());
        } else if (state.equals(State.FUTURE.name())) {
            bookings = bookingRepository.getAllByOwnerFuture(id);
        } else if (state.equals(State.WAITING.name())) {
            bookings = bookingRepository.getAllByOwnerWaiting(id);
        } else if (state.equals(State.REJECTED.name())) {
            bookings = bookingRepository.getAllByOwnerRejected(id);
        } else {
            throw new IllegalAccessException("Unknown state: " + state);
        }
        for (Booking booking : bookings) {
            bookingDtoList.add(getById(booking.getId(), id));
        }
        return bookingDtoList;
    }

    @Override
    public List<BookingDto> getAllByBooker(Long id, String state) throws IllegalAccessException {
        userService.getById(id);
        List<Booking> bookings;
        List<BookingDto> bookingDtoList = new ArrayList<>();
        if (state.equals(State.ALL.name())) {
            bookings = bookingRepository.getAllByBookerAll(id);
        } else if (state.equals(State.CURRENT.name())) {
            bookings = bookingRepository.getAllByBookerCurrent(id, LocalDateTime.now());
        } else if (state.equals(State.PAST.name())) {
            bookings = bookingRepository.getAllByBookerPast(id, LocalDateTime.now());
        } else if (state.equals(State.FUTURE.name())) {
            bookings = bookingRepository.getAllByBookerFuture(id);
        } else if (state.equals(State.WAITING.name())) {
            bookings = bookingRepository.getAllByBookerWaiting(id);
        } else if (state.equals(State.REJECTED.name())) {
            bookings = bookingRepository.getAllByBookerRejected(id);
        } else {
            throw new IllegalAccessException("Unknown state: " + state);
        }
        for (Booking booking : bookings) {
            bookingDtoList.add(getById(booking.getId(), id));
        }
        return bookingDtoList;
    }
}
