package ru.practicum.shareit.validation;

import lombok.ToString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;

@ToString
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {
    @Mock
    ValidationService validationService;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemService itemService;


    BookingDto bookingDto = mock(BookingDto.class);
    private final User user1 = new User(1L, "User1", "user1@email.com");
    private final User user2 = new User(2L, "User2", "user2@email.com");
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

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void checkUniqueEmailUserAdd_WhenEmailIsNull_ThenReturnedValidateException() {
        user1.setEmail(null);

        doThrow(new ValidateException("Ошибка! Пользователь с пустым e-mail не может быть добавлен!"))
                .when(validationService).checkUniqueEmailUserAdd(user1);
    }

    @Test
    void checkUniqueEmailUserAdd_WhenEmailIsBlank_ThenReturnedValidateException() {
        user1.setEmail("");

        doThrow(new ValidateException("Ошибка! Пользователь с пустым e-mail не может быть добавлен!"))
                .when(validationService).checkUniqueEmailUserAdd(user1);
    }

    @Test
    void checkOwnerItemAndBooker_WhenUserIsNotOwner_() {

        doThrow(new NotFoundException("Вносить изменения в параметры вещи может только владелец!"))
                .when(validationService).checkOwnerItemAndBooker(item.getId(), user1.getId(), user2.getId());
    }

    @Test
    void checkOwnerItemAndBooker_WhenUserIsOwner_ThenReturnedNotFoundException() {
        doThrow(new NotFoundException("Вносить изменения в параметры вещи может только владелец!"))
                .when(validationService).checkOwnerItemAndBooker(item.getId(), user1.getId(), user1.getId());
    }

    @Test
    void checkBookerIsTheOwner_WhenBookerIsNotOwnerItem() {
        doThrow(new NotFoundException("Ошибка! Невозможно добавить бронирование!"))
                .when(validationService).checkBookerIsTheOwner(item, user2.getId());
    }

    @Test
    void checkBookerIsTheOwner_WhenBookerIsOwnerItem_() {
        doThrow(new NotFoundException("Ошибка! Невозможно добавить бронирование!"))
                .when(validationService).checkBookerIsTheOwner(item, user1.getId());
    }

    @Test
    void checkBookerOrOwner() {
        doThrow(new NotFoundException("Просматривать информацию о бронированнии вещи может только владелец" +
                " или клиент бронирования!"))
                .when(validationService).checkBookerOrOwner(user1.getId(), booking.getId());
    }

    @Test
    void checkItemDtoWhenAdd_WhereAreTheEmptyFields_ThenReturnedNotFoundException() {
        ItemDto itemDto = toItemDto(item);
        itemDto.setAvailable(null);

        doThrow(new NotFoundException("Ошибка! Вещь с пустыми полями не может быть добавлена!"))
                .when(validationService).checkItemDtoWhenAdd(itemDto);

        itemDto.setAvailable(true);
        itemDto.setName(null);

        doThrow(new NotFoundException("Ошибка! Вещь с пустыми полями не может быть добавлена!"))
                .when(validationService).checkItemDtoWhenAdd(itemDto);

        itemDto.setName("");

        doThrow(new NotFoundException("Ошибка! Вещь с пустыми полями не может быть добавлена!"))
                .when(validationService).checkItemDtoWhenAdd(itemDto);

        itemDto.setName("itemDto");
        itemDto.setDescription(null);

        doThrow(new NotFoundException("Ошибка! Вещь с пустыми полями не может быть добавлена!"))
                .when(validationService).checkItemDtoWhenAdd(itemDto);

        itemDto.setDescription("");

        doThrow(new NotFoundException("Ошибка! Вещь с пустыми полями не может быть добавлена!"))
                .when(validationService).checkItemDtoWhenAdd(itemDto);

        itemDto.setDescription(item.getDescription());
    }

    @Test
    void checkBookingDtoWhenAdd_WhereInvalidTimeAndDate_ThenReturnedValidateException() {
        validationService = mock(ValidationService.class);
        BookingDto bookingDtoBadTime = toBookingDto(booking);
        bookingDtoBadTime.setStart(null);

        doThrow(new ValidateException("Ошибка! Указано неправильно дата или время бронирования!"))
                .when(validationService).checkBookingDtoWhenAdd(bookingDtoBadTime);

        bookingDtoBadTime.setStart(LocalDateTime.now());
        bookingDtoBadTime.setEnd(null);

        doThrow(new ValidateException("Ошибка! Указано неправильно дата или время бронирования!"))
                .when(validationService).checkBookingDtoWhenAdd(bookingDtoBadTime);

        bookingDtoBadTime.setStart(LocalDateTime.now());
        bookingDtoBadTime.setEnd(LocalDateTime.now().plusHours(1));

        doThrow(new ValidateException("Ошибка! Указано неправильно дата или время бронирования!"))
                .when(validationService).checkBookingDtoWhenAdd(bookingDtoBadTime);

        bookingDtoBadTime.setStart(LocalDateTime.now());
        bookingDtoBadTime.setEnd(LocalDateTime.now());

        doThrow(new ValidateException("Ошибка! Указано неправильно дата или время бронирования!"))
                .when(validationService).checkBookingDtoWhenAdd(bookingDtoBadTime);

        bookingDtoBadTime.setStart(LocalDateTime.now().minusHours(1));
        bookingDtoBadTime.setEnd(LocalDateTime.now());

        doThrow(new ValidateException("Ошибка! Указано неправильно дата или время бронирования!"))
                .when(validationService).checkBookingDtoWhenAdd(bookingDtoBadTime);

        bookingDtoBadTime.setStart(LocalDateTime.now());
        bookingDtoBadTime.setEnd(LocalDateTime.now().minusHours(1));

        doThrow(new ValidateException("Ошибка! Указано неправильно дата или время бронирования!"))
                .when(validationService).checkBookingDtoWhenAdd(bookingDtoBadTime);
    }

    @Test
    void checkStatusState() {
    }

    @Test
    void checkTheUserRentedTheItem() {
    }

    @Test
    void checkCommentText() {
    }
}