package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BookingForItemDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Long bookerId;
    private Status status;
}
