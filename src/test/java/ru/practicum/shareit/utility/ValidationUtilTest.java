package ru.practicum.shareit.utility;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.StateStatusValidateException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.utility.ValidationUtil.*;


@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ValidationUtilTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    private String textStatus;
    private final User user1 = new User(1L, "User1", "user1@email.com");
    private final User user2 = new User(2L, "User2", "user2@email.com");
    private final User user3 = new User(3L, "User3", "user3@email.com");
    private final Item item = Item.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(user1)
            .build();

    private final Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .item(item)
            .booker(user2)
            .status(Status.WAITING)
            .build();

    @Test
    void checkUniqueEmailUserAdd_WhenEmailIsNull_ThenReturnedValidateException() {
        user1.setEmail(null);

        assertThrows(ValidateException.class,
                () -> checkUniqueEmailUserAdd(user1));
    }

    @Test
    void checkUniqueEmailUserAdd_WhenEmailIsBlank_ThenReturnedValidateException() {
        user1.setEmail("");

        assertThrows(ValidateException.class,
                () -> checkUniqueEmailUserAdd(user1));
        user1.setEmail("user1@ya.ru");
    }

    @Test
    void checkBookerIsTheOwner_WhenBookerIsOwnerItem_ThenReturnedNotFoundException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user1));

        assertThrows(NotFoundException.class,
                () -> checkBookerIsTheOwner(item, user1.getId()));
    }

    @Test
    void checkItemDtoWhenAdd_WhereAreTheEmptyFields_ThenReturnedNotFoundException() {
        item.setAvailable(null);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(ValidateException.class,
                () -> checkItemDtoWhenAdd(toItemDto(item)));

        item.setAvailable(true);
        item.setName(null);

        assertThrows(ValidateException.class,
                () -> checkItemDtoWhenAdd(toItemDto(item)));

        item.setName("");

        assertThrows(ValidateException.class,
                () -> checkItemDtoWhenAdd(toItemDto(item)));

        item.setName("itemDto");
        item.setDescription(null);

        assertThrows(ValidateException.class,
                () -> checkItemDtoWhenAdd(toItemDto(item)));

        item.setDescription("");

        assertThrows(ValidateException.class,
                () -> checkItemDtoWhenAdd(toItemDto(item)));

        item.setDescription(item.getDescription());
    }

    @Test
    void checkBookingDtoWhenAdd_WhereInvalidTimeAndDate_ThenReturnedValidateException() {

        BookingDto bookingDtoBadTime = toBookingDto(booking);
        bookingDtoBadTime.setStart(null);

        assertThrows(ValidateException.class,
                () -> checkBookingDtoWhenAdd(bookingDtoBadTime));

        bookingDtoBadTime.setStart(LocalDateTime.now());
        bookingDtoBadTime.setEnd(null);

        assertThrows(ValidateException.class,
                () -> checkBookingDtoWhenAdd(bookingDtoBadTime));

        bookingDtoBadTime.setStart(LocalDateTime.now());
        bookingDtoBadTime.setEnd(LocalDateTime.now().minusHours(1));

        assertThrows(ValidateException.class,
                () -> checkBookingDtoWhenAdd(bookingDtoBadTime));

        bookingDtoBadTime.setStart(LocalDateTime.now());
        bookingDtoBadTime.setEnd(LocalDateTime.now());

        assertThrows(ValidateException.class,
                () -> checkBookingDtoWhenAdd(bookingDtoBadTime));

        bookingDtoBadTime.setStart(LocalDateTime.now().minusHours(1));
        bookingDtoBadTime.setEnd(LocalDateTime.now());

        assertThrows(ValidateException.class,
                () -> checkBookingDtoWhenAdd(bookingDtoBadTime));

        bookingDtoBadTime.setStart(LocalDateTime.now());
        bookingDtoBadTime.setEnd(LocalDateTime.now().minusHours(1));

        assertThrows(ValidateException.class,
                () -> checkBookingDtoWhenAdd(bookingDtoBadTime));
    }

    @Test
    void checkStatusState_WhereInvalidStatus_ThenReturnedStateStatusValidateException() {
        textStatus = "Ok";
        assertThrows(StateStatusValidateException.class,
                () -> checkStatusState(textStatus));
    }

    @Test
    void checkTheUserRentedTheItem_WhereIsNotRentedItem_ThenReturnedValidateException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user1));

        assertThrows(ValidateException.class,
                () -> checkTheUserRentedTheItem(user1.getId(), item));
    }

    @Test
    void checkCommentText_WhereCommentTextIsNotValid_ThenReturnedValidateException() {
        textStatus = "";
        assertThrows(ValidateException.class,
                () -> checkCommentText(textStatus));

        textStatus = null;
        assertThrows(ValidateException.class,
                () -> checkCommentText(textStatus));
    }
}