package ru.practicum.shareit.utility;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.StatusDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.utility.ValidationUtil.checkBookerIsTheOwner;
import static ru.practicum.shareit.utility.ValidationUtil.checkTheUserRentedTheItem;


@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ValidationUtilTest {

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
            .status(StatusDto.WAITING)
            .build();

    @Test
    void checkBookerIsTheOwner_WhenBookerIsOwnerItem_ThenReturnedNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> checkBookerIsTheOwner(item, user1.getId()));
    }

    @Test
    void checkTheUserRentedTheItem_WhereIsNotRentedItem_ThenReturnedValidateException() {
        assertThrows(ValidateException.class,
                () -> checkTheUserRentedTheItem(user1.getId(), item));
    }

}