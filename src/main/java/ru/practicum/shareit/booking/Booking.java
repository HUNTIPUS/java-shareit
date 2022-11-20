package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_time")
    private LocalDateTime start;
    @Column(name = "end_time")
    private LocalDateTime end;
    @Column(name = "item_id", nullable = false)
    private Long itemId;
    @Column(name = "booker_id")
    private Long bookerId;
    @Enumerated(EnumType.STRING)
    private Status status;
}
