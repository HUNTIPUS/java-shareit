package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b join Item i on i.id = b.itemId " +
            "where b.id = ?1 and (i.ownerId = ?2 or b.bookerId = ?2)")
    Booking getById(Long bookingId, Long userId);

    @Query("select b from Booking b join Item i on i.id = b.itemId " +
            "where i.ownerId = ?1 order by b.start DESC")
    List<Booking> getAllByOwnerAll(Long ownerId);

    @Query("select b from Booking b join Item i on i.id = b.itemId " +
            "where i.ownerId = ?1 and b.end >= ?2 and b.start <= ?2 order by b.start DESC")
    List<Booking> getAllByOwnerCurrent(Long ownerId, LocalDateTime time);

    @Query("select b from Booking b join Item i on i.id = b.itemId " +
            "where i.ownerId = ?1 and (b.status = 'APPROVED' or b.status = 'WAITING') order by b.start DESC")
    List<Booking> getAllByOwnerFuture(Long ownerId);

    @Query("select b from Booking b join Item i on i.id = b.itemId " +
            "where i.ownerId = ?1 and b.status = 'APPROVED' and b.end < ?2 order by b.start DESC")
    List<Booking> getAllByOwnerPast(Long ownerId, LocalDateTime time);

    @Query("select b from Booking b join Item i on i.id = b.itemId " +
            "where i.ownerId = ?1 and b.status = 'WAITING' order by b.start DESC")
    List<Booking> getAllByOwnerWaiting(Long ownerId);

    @Query("select b from Booking b join Item i on i.id = b.itemId " +
            "where i.ownerId = ?1 and (b.status = 'REJECTED' or b.status = 'CANCELED') order by b.start DESC")
    List<Booking> getAllByOwnerRejected(Long ownerId);

    @Query("select b from Booking b " +
            "where b.bookerId = ?1 order by b.start DESC")
    List<Booking> getAllByBookerAll(Long bookerId);

    @Query("select b from Booking b " +
            "where b.bookerId = ?1 and b.end >= ?2 and b.start <= ?2 order by b.start DESC")
    List<Booking> getAllByBookerCurrent(Long bookerId, LocalDateTime time);

    @Query("select b from Booking b join Item i on i.id = b.itemId " +
            "where b.bookerId = ?1 and (b.status = 'APPROVED' or b.status = 'WAITING') order by b.start DESC")
    List<Booking> getAllByBookerFuture(Long bookerId);

    @Query("select b from Booking b " +
            "where b.bookerId = ?1 and b.status = 'APPROVED' and b.end < ?2 order by b.start DESC")
    List<Booking> getAllByBookerPast(Long bookerId, LocalDateTime time);

    @Query("select b from Booking b " +
            "where b.bookerId = ?1 and b.status = 'WAITING' order by b.start DESC")
    List<Booking> getAllByBookerWaiting(Long bookerId);

    @Query("select b from Booking b " +
            "where b.bookerId = ?1 and (b.status = 'REJECTED' or b.status = 'CANCELED') order by b.start DESC")
    List<Booking> getAllByBookerRejected(Long bookerId);

    @Query("select b from Booking b where b.itemId = ?1 and b.bookerId <> ?2 order by b.start desc")
    List<Booking> getBookingByItemId(Long itemId, Long ownerId);
}
