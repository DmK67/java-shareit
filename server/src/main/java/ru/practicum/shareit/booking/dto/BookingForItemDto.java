package ru.practicum.shareit.booking.dto;

import lombok.*;

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
    private StatusDto status;
}
