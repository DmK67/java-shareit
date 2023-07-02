package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class BookingForItemDto {
    private Long id;
    @JsonAlias({"start"})
    private LocalDateTime startTime;
    @JsonAlias({"end"})
    private LocalDateTime endTime;
    private Long bookerId;
    private Status status;
}
