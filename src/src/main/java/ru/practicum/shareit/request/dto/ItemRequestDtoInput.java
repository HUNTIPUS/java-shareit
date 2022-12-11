package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ItemRequestDtoInput {

    private Long requestorId;
    @NotBlank
    private String description;
    private LocalDateTime created = LocalDateTime.now();
}
