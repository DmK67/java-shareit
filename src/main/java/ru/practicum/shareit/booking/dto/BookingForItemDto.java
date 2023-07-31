package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class BookingForItemDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long bookerId;
    private Status status;
}
