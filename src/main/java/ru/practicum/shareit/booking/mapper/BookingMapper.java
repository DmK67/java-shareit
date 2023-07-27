package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Component
@AllArgsConstructor

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {// Метод перевода объекта booking в объект bookingDto
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(ItemDto.builder().id(booking.getItem().getId()).name(booking.getItem().getName()).build())
                .itemId(booking.getItem().getId())
                .booker(toUserDto(booking.getBooker()))
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto) { // Метод перевода объекта bookingDto в объект booking
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

    public static BookingForItemDto toBookingForItemDto(Booking booking) {
        if (booking != null) {
            return BookingForItemDto.builder()
                    .id(booking.getId())
                    .startTime(booking.getStart())
                    .bookerId(booking.getBooker().getId())
                    .status(booking.getStatus())
                    .build();
        } else {
            return null;
        }
    }

    public static List<Booking> listResultAddItemAndAddBooker(List<Booking> listResult) {
        for (Booking booking : listResult) {
            Item item = booking.getItem();
            booking.setItem(Item.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .build());
            User user = booking.getBooker();
            booking.setBooker(User.builder().id(user.getId()).build());
        }
        return listResult;
    }
}
