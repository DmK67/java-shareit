package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repositiry.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationService;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.item.comment.mapper.CommentMapper.toComment;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    private final ItemServiceImpl itemService;

    private final UserService userService;

    UserRepository userRepository;
    ItemRepository itemRepository;
    CommentRepository commentRepository;
    ValidationService validationService;
    LocalDateTime now = LocalDateTime.now();

    Item item1;

    User user1;

    UserDto userDto1;

    ItemDto itemDto1;

    //User owner;

    @BeforeEach
    void setUp() {
        userDto1 = UserDto.builder()
                .id(1L)
                .name("userDto1")
                .email("userDto1@ya.ru")
                .build();

        user1 = toUser(userDto1);

        itemDto1 = ItemDto.builder()
                .id(1L)
                .name("itemDto1")
                .description("description1")
                .available(true)
                .build();

        item1 = toItem(itemDto1);

    }

    @Test
    void addItem_WhenAllIsOk_ThenReturnedAddedItem() {
        userService.addUser(user1);
        item1.setId(999L);

        Item itemFromDb = itemService.addItem(item1, user1.getId());

        assertEquals(1, itemFromDb.getId());
        assertEquals(item1.getName(), itemFromDb.getName());
        assertEquals(item1.getDescription(), itemFromDb.getDescription());
    }

    @Test
    void addItem_WhenAvailableIsNull_ThenReturnValidateException() {
        item1.setAvailable(null);

        assertThrows(ValidateException.class,
                () -> itemService.addItem(item1, user1.getId()));
    }

    @Test
    void addItem_WhenItemDtoNameIsNull_ThenReturnValidateException() {
        itemDto1.setName(null);

        assertThrows(ValidateException.class,
                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
    }

    @Test
    void addItem_WhenItemDtoNameIsBlank_ThenReturnValidateException() {
        itemDto1.setName("");

        assertThrows(ValidateException.class,
                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
        itemDto1.setName("itemDto1");
    }

    @Test
    void addItem_WhenItemDtoDescriptionIsNull_ThenReturnValidateException() {
        itemDto1.setDescription(null);

        assertThrows(ValidateException.class,
                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
    }

    @Test
    void addItem_WhenItemDtoDescriptionIsBlank_ThenReturnValidateException() {
        itemDto1.setDescription("");

        assertThrows(ValidateException.class,
                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
    }

    @Test
    void getItemById_WhenItemIsOk_ThenReturnedItem() {
        userService.addUser(user1);
        //userService.addUser(owner);
        Item savedItem = itemService.addItem(item1, user1.getId());

        Item item = itemService.getItemById(savedItem.getId());

        assertNotNull(item.getId());
        assertEquals(1L, item.getId());
        assertEquals(item.getName(), item1.getName());
        assertEquals(item.getDescription(), item1.getDescription());
        assertTrue(item.getAvailable());
    }

    @Test
    void getItemById_WhenItemIsNotFound_ThenReturnedNotFoundException() {
        userService.addUser(user1);
        itemService.addItem(item1, user1.getId());

        assertThrows(NotFoundException.class,
                () -> itemService.getItemById(9000L));
    }

    @Test
    void getItemById_WhenUserIsNotFound_ThenReturnedNotFoundException() {

        assertThrows(NotFoundException.class,
                () -> itemService.addItem(item1, user1.getId()));
    }

    @Test
    void getItemByIdWithBooking_WhenItemIsOk_ThenReturneItemWithBookingDto() { // Дописать !!!
        userService.addUser(user1);
        Item savedItem = itemService.addItem(item1, user1.getId());

        Item item = itemService.getItemById(savedItem.getId());

        assertNotNull(item.getId());
        assertEquals(1L, item.getId());
        assertEquals(item.getName(), item1.getName());
        assertEquals(item.getDescription(), item1.getDescription());
        assertTrue(item.getAvailable());
    }

    @Test
    void updateItem() {
    }

    @Test
    void getListItemsUserById() {
    }

    @Test
    void getSearchItems() {
    }

    @Test
    void checkingIsAvailable() {
    }

    @Test
    void addComment() {
    }
}