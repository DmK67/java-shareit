package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static ru.practicum.shareit.item.mapper.ItemMapper.itemWithItemDtoForBooking;
import static ru.practicum.shareit.user.mapper.UserMapper.userWithUserDtoForBooking;

@Component
@AllArgsConstructor

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) { // Метод перевода объекта booking в объект bookingDto
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                //.booker(userWithUserDtoForBooking(booking.getBooker()))
                .booker(User.builder().id(booking.getBooker().getId()).build())
                .status(booking.getStatus())
                .statusState((booking.getStatusState()))
                //.item(itemWithItemDtoForBooking(booking.getItem()))
                .item(Item.builder().id(booking.getItem().getId()).name(booking.getItem().getName()).build())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto) {// Метод перевода объекта bookingDto в объект booking
        Item item = Item.builder()
                .id(bookingDto.getItemId())
                .build();
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .status(bookingDto.getStatus())
                .statusState(bookingDto.getStatusState())
                .build();
    }

    public static BookingForItemDto toBookingForItemDto(Booking booking) {

        return BookingForItemDto.builder()
                .id(booking.getId())
                .startTime(booking.getStart())
                .bookerId(booking.getBooker().getId())
                .status(booking.getStatus())
                .build();
    }
}
