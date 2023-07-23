package ru.practicum.shareit.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;
    @Mock
    private BookingService bookingService;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ValidationService validationService;

    private final User user1 = new User(1L, "User1", "user1@email.com");
    private final User user2 = new User(2L, "User2", "user2@email.com");
    private final UserDto userDto = new UserDto(1L, "UserDto", "userDto@email.com");
    private final BookingDto bookingDto = BookingDto.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1L))
            .itemId(1L)
            .build();
    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .requestId(1L)
            .build();
    private final Item item = Item.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(user1)
            .build();

    private final Item item2 = Item.builder()
            .id(1L)
            .name("Item2")
            .description("Description2")
            .available(true)
            .owner(user2)
            .build();

    private final Booking booking1 = Booking.builder()
            .booker(user1)
            .id(1L)
            .status(Status.APPROVED)
            .item(item).build();


    @Test
    void checkTimeIsNotValidThenReturnedValidateException() {
        BookingDto bookingBadTime = BookingDto.builder()
                .start(LocalDateTime.now().plusHours(1L))
                .end(LocalDateTime.now().minusHours(1L))
                .itemId(1L)
                .build();

        Exception e = assertThrows(ValidateException.class,
                () -> validationService.checkBookingDtoWhenAdd(bookingBadTime));
        assertEquals(e.getMessage(), String.format("Ошибка! Указано неправильно дата или время бронирования!",
                bookingBadTime.getStart(), bookingBadTime.getEnd()));
    }

    @Test
    void createBookingWhenUserIsNotOwnerThenReturnedOperationAccessException() {
        final User userBad = new User(123L, "UserBad", "userBad@email.com");
        Mockito.when(bookingService.getBookingById(anyLong()))
                .thenReturn(booking1);
        Mockito.when(itemService.getItemById(anyLong()))
                .thenReturn(item);

        Exception e = assertThrows(NoSuchElementException.class,
                () -> validationService.checkBookerOrOwner(userBad.getId(), booking1.getId()));

        assertEquals(e.getMessage(), "Просматривать информацию о бронированнии вещи может только владелец" +
                " или клиент бронирования!");
    }
//when(itemRepository.findById(booking.getItem().getId())).thenReturn(Optional.of(item));
//
//        assertThrows(EntityNotFoundException.class, () -> bookingService.postBooking(userId, booking));

}