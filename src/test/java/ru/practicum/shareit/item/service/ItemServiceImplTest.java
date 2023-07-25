package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
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

    @DirtiesContext
    @Test
    void addItem_WhenAllIsOk_ThenReturnedAddedItem() {
        userService.addUser(user1);
        item1.setId(999L);

        Item itemFromDb = itemService.addItem(item1, user1.getId());

        assertEquals(1, itemFromDb.getId());
        assertEquals(item1.getName(), itemFromDb.getName());
        assertEquals(item1.getDescription(), itemFromDb.getDescription());
    }
    @DirtiesContext
    @Test
    void addItem_WhenAvailableIsNull_ThenReturnValidateException() {
        item1.setAvailable(null);

        assertThrows(ValidateException.class,
                () -> itemService.addItem(item1, user1.getId()));
    }
    @DirtiesContext
    @Test
    void addItem_WhenItemDtoNameIsNull_ThenReturnValidateException() {
        itemDto1.setName(null);

        assertThrows(ValidateException.class,
                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
    }
    @DirtiesContext
    @Test
    void addItem_WhenItemDtoNameIsBlank_ThenReturnValidateException() {
        itemDto1.setName("");

        assertThrows(ValidateException.class,
                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
        itemDto1.setName("itemDto1");
    }
    @DirtiesContext
    @Test
    void addItem_WhenItemDtoDescriptionIsNull_ThenReturnValidateException() {
        itemDto1.setDescription(null);

        assertThrows(ValidateException.class,
                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
    }
    @DirtiesContext
    @Test
    void addItem_WhenItemDtoDescriptionIsBlank_ThenReturnValidateException() {
        itemDto1.setDescription("");

        assertThrows(ValidateException.class,
                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
    }
    @DirtiesContext
    @Test
    void getItemById_WhenItemIsOk_ThenReturnedItem() {
        userService.addUser(user1);
        Item savedItem = itemService.addItem(item1, user1.getId());

        Item item = itemService.getItemById(savedItem.getId());

        assertNotNull(item.getId());
        assertEquals(1L, item.getId());
        assertEquals(item.getName(), item1.getName());
        assertEquals(item.getDescription(), item1.getDescription());
        assertTrue(item.getAvailable());
    }
    @DirtiesContext
    @Test
    void getItemById_WhenItemIsNotFound_ThenReturnedNotFoundException() {

        assertThrows(NotFoundException.class,
                () -> itemService.getItemById(9000L));
    }
    @DirtiesContext
    @Test
    void getItemById_WhenUserIsNotFound_ThenReturnedNotFoundException() {

        assertThrows(NotFoundException.class,
                () -> itemService.addItem(item1, user1.getId()));
    }
    @DirtiesContext
    @Test
    void getItemByIdWithBooking_WhenItemIsOk_ThenReturneItemWithBookingDto() {
        userService.addUser(user1);
        Item savedItem = itemService.addItem(item1, user1.getId());

        Item item = itemService.getItemById(savedItem.getId());

        assertNotNull(item.getId());
        assertEquals(1L, item.getId());
        assertEquals(item.getName(), item1.getName());
        assertEquals(item.getDescription(), item1.getDescription());
        assertTrue(item.getAvailable());
    }

    @DirtiesContext
    @Test
    void getItemByIdWithBooking_WhenItemIsNotFound_ThenReturnedNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemService.getItemById(9000L));
    }

    @DirtiesContext
    @Test
    void getItemByIdWithBooking_WhenUserIsNotFound_ThenReturnedNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemService.addItem(item1, user1.getId()));
    }

    @DirtiesContext
    @Test
    void updateItem_WhenAllFildsIsOk_ThenReturnItemFromDb() {
        User savedOwner = userService.addUser(user1);
        Item savedItemBeforeUpdate = itemService.addItem(item1, savedOwner.getId());

        Item updatedItem = Item.builder()
                .id(savedItemBeforeUpdate.getId())
                .name("update name")
                .description("update description")
                .build();
        Item itemAfterUpdate = itemService.updateItem(updatedItem, updatedItem.getId(), savedOwner.getId());

        assertEquals(savedItemBeforeUpdate.getName(), itemAfterUpdate.getName());
        assertEquals(savedItemBeforeUpdate.getDescription(), itemAfterUpdate.getDescription());
        assertEquals(savedItemBeforeUpdate.getId(), itemAfterUpdate.getId());
        assertEquals(savedItemBeforeUpdate.getAvailable(), itemAfterUpdate.getAvailable());
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