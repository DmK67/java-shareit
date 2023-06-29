package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Component
@AllArgsConstructor

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) { // Метод перевода объекта booking в объект bookingDto
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .item(booking.getItem())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto) {// Метод перевода объекта userDto в объект user
        Item item = Item.builder()
                .id(bookingDto.getItemId())
                .build();
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .status(bookingDto.getStatus())
                .build();
    }
}
