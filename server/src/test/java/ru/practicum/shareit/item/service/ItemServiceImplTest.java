package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
class ItemServiceImplTest {

    private final ItemServiceImpl itemService;
    private final ItemServiceImpl itemServiceImpl;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    LocalDateTime now = LocalDateTime.now();

    Item item1;
    Item item2;
    User user1;
    User user2;
    UserDto userDto1;
    UserDto userDto2;
    ItemDto itemDto1;
    ItemDto itemDto2;
    Comment comment;
    BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        userDto1 = UserDto.builder().id(1L).name("userDto1").email("userDto1@ya.ru").build();
        user1 = toUser(userDto1);

        userDto2 = UserDto.builder().id(2L).name("userDto2").email("userDto2@ya.ru").build();
        user2 = toUser(userDto2);

        itemDto1 = ItemDto.builder().id(1L).name("itemDto1").description("description1").available(true).build();
        item1 = toItem(itemDto1);

        itemDto2 = ItemDto.builder().id(2L).name("itemDto2").description("description2").available(true).build();
        item2 = toItem(itemDto2);

        comment = Comment.builder().text("comment").created(LocalDateTime.now()).build();
    }

    @DirtiesContext
    @Test
    void addItem_WhenAllIsOk_ThenReturnedAddedItem() {
        userService.addUser(user1);

        Item itemFromDb = itemService.addItem(item1, user1.getId());

        assertEquals(1, itemFromDb.getId());
        assertEquals(item1.getName(), itemFromDb.getName());
        assertEquals(item1.getDescription(), itemFromDb.getDescription());
    }

//    @DirtiesContext
//    @Test
//    void addItem_WhenAvailableIsNull_ThenReturnValidateException() {
//        item1.setAvailable(null);
//
//        assertThrows(ValidateException.class,
//                () -> itemService.addItem(item1, user1.getId()));
//    }

//    @DirtiesContext
//    @Test
//    void addItem_WhenItemDtoNameIsNull_ThenReturnValidateException() {
//        itemDto1.setName(null);
//
//        assertThrows(ValidateException.class,
//                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
//    }

//    @DirtiesContext
//    @Test
//    void addItem_WhenItemDtoNameIsBlank_ThenReturnValidateException() {
//        itemDto1.setName("");
//
//        assertThrows(ValidateException.class,
//                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
//        itemDto1.setName("itemDto1");
//    }

//    @DirtiesContext
//    @Test
//    void addItem_WhenItemDtoDescriptionIsNull_ThenReturnValidateException() {
//        itemDto1.setDescription(null);
//
//        assertThrows(ValidateException.class,
//                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
//    }

//    @DirtiesContext
//    @Test
//    void addItem_WhenItemDtoDescriptionIsBlank_ThenReturnValidateException() {
//        itemDto1.setDescription("");
//
//        assertThrows(ValidateException.class,
//                () -> itemService.addItem(toItem(itemDto1), user1.getId()));
//    }

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

//    @DirtiesContext
//    @Test
//    void getItemById_WhenUserIsNotFound_ThenReturnedNotFoundException() {
//
//        assertThrows(NotFoundException.class,
//                () -> itemService.addItem(item1, user1.getId()));
//    }

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

//    @DirtiesContext
//    @Test
//    void getItemByIdWithBooking_WhenItemIsNotFound_ThenReturnedNotFoundException() {
//        assertThrows(NotFoundException.class,
//                () -> itemService.getItemById(9000L));
//    }

//    @DirtiesContext
//    @Test
//    void getItemByIdWithBooking_WhenUserIsNotFound_ThenReturnedNotFoundException() {
//        assertThrows(NotFoundException.class,
//                () -> itemService.addItem(item1, user1.getId()));
//    }

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

//    @DirtiesContext
//    @Test
//    void updateItem_WhenItemNotFound_ThenReturnedNotFoundException() {
//        assertThrows(NotFoundException.class,
//                () -> itemService.updateItem(item1, item1.getId(), user1.getId()));
//    }

//    @DirtiesContext
//    @Test
//    void updateItem_WhenUserNotFound_ThenReturnedNotFoundException() {
//        assertThrows(NotFoundException.class,
//                () -> itemService.updateItem(item1, item1.getId(), user1.getId()));
//    }

    @DirtiesContext
    @Test
    void updateItem_WhenUserIsOwner_ThenReturnedForbiddenException() {
        User user1FromBd = userService.addUser(user1);
        User user2FromBd = userService.addUser(user2);

        Item itemFromBd = itemService.addItem(item1, user1FromBd.getId());
        Item updateItem = Item.builder()
                .id(itemFromBd.getId())
                .name("updateItem")
                .build();

        assertThrows(ForbiddenException.class,
                () -> itemService.updateItem(updateItem, itemFromBd.getId(), user2FromBd.getId()));
    }

    @DirtiesContext
    @Test
    void updateItem_WhenItemNameIsNull_ThenReturnedItemFromBd() {
        User userFromBd = userService.addUser(user1);
        Item itemBeforeUpdateFromBd = itemService.addItem(item1, userFromBd.getId());
        Item updatedItem = Item.builder()
                .id(itemBeforeUpdateFromBd.getId())
                .name(null)
                .description("update description")
                .build();

        Item itemAfterUpdateFromBd = itemService.updateItem(updatedItem, updatedItem.getId(), userFromBd.getId());

        assertEquals(itemBeforeUpdateFromBd.getName(), itemAfterUpdateFromBd.getName());
    }

//    @DirtiesContext
//    @Test
//    void updateItem_WhenItemNameIsBlank_ThenReturnedItemFromBd() {
//        User userFromBd = userService.addUser(user1);
//        Item itemBeforeUpdateFromBd = itemService.addItem(item1, userFromBd.getId());
//        Item updatedItem = Item.builder()
//                .id(itemBeforeUpdateFromBd.getId())
//                .name("")
//                .description("update description")
//                .build();
//
//        Item itemAfterUpdateFromBd = itemService.updateItem(updatedItem, updatedItem.getId(), userFromBd.getId());
//
//        assertEquals(itemBeforeUpdateFromBd.getName(), itemAfterUpdateFromBd.getName());
//    }

//    @DirtiesContext
//    @Test
//    void updateItem_WhenItemDescriptionIsNull_ThenReturnedItemFromBd() {
//        User userFromBd = userService.addUser(user1);
//        Item itemBeforeUpdateFromBd = itemService.addItem(item1, userFromBd.getId());
//        Item updatedItem = Item.builder()
//                .id(itemBeforeUpdateFromBd.getId())
//                .name("updateItem")
//                .description(null)
//                .build();
//
//        Item itemAfterUpdateFromBd = itemService.updateItem(updatedItem, updatedItem.getId(), userFromBd.getId());
//
//        assertEquals(itemBeforeUpdateFromBd.getDescription(), itemAfterUpdateFromBd.getDescription());
//    }

//    @DirtiesContext
//    @Test
//    void updateItem_WhenUpdateItemDescriptionIsNotItemFromDbDescription_ThenReturnedItemFromBd() {
//        User userFromBd = userService.addUser(user1);
//        Item itemBeforeUpdateFromBd = itemService.addItem(item1, userFromBd.getId());
//        itemBeforeUpdateFromBd.setDescription("Description before update");
//        Item updatedItem = Item.builder()
//                .id(itemBeforeUpdateFromBd.getId())
//                .name("updateItem")
//                .description("Description after update")
//                .build();
//
//        Item itemAfterUpdateFromBd = itemService.updateItem(updatedItem, updatedItem.getId(), userFromBd.getId());
//
//        assertEquals("Description after update", itemAfterUpdateFromBd.getDescription());
//    }

//    @DirtiesContext
//    @Test
//    void updateItem_WhenUpdateItemNameIsNotItemFromDbName_ThenReturnedItemFromBd() {
//        User userFromBd = userService.addUser(user1);
//        Item itemBeforeUpdateFromBd = itemService.addItem(item1, userFromBd.getId());
//        itemBeforeUpdateFromBd.setName("Name before update");
//        Item updatedItem = Item.builder()
//                .id(itemBeforeUpdateFromBd.getId())
//                .name("Name after update")
//                .build();
//
//        Item itemAfterUpdateFromBd = itemService.updateItem(updatedItem, updatedItem.getId(), userFromBd.getId());
//
//        assertEquals("Name after update", itemAfterUpdateFromBd.getName());
//    }

    @DirtiesContext
    @Test
    void updateItem_WhenUpdateItemOwner_ThenReturnedItemFromBd() {
        User ownerFromBd = userService.addUser(user1);
        Item itemBeforeUpdateFromBd = itemService.addItem(item1, ownerFromBd.getId());
        Item updatedItem = Item.builder()
                .id(itemBeforeUpdateFromBd.getId())
                .name("Name after update")
                .build();

        Item itemAfterUpdateFromBd = itemService.updateItem(updatedItem, updatedItem.getId(), ownerFromBd.getId());

        assertEquals(itemBeforeUpdateFromBd.getOwner().getId(), itemAfterUpdateFromBd.getOwner().getId());
        assertEquals(itemBeforeUpdateFromBd.getOwner().getName(), itemAfterUpdateFromBd.getOwner().getName());
        assertEquals(itemBeforeUpdateFromBd.getOwner().getEmail(), itemAfterUpdateFromBd.getOwner().getEmail());
    }

    @DirtiesContext
    @Test
    void getListItemsUserById_WhenIsOk_ThenReturnedItemListWithBookingDto() {
        User user1FromBd = userService.addUser(user1);
        Item item1FromDb = itemService.addItem(item1, user1FromBd.getId());
        item1FromDb.setComments(new ArrayList<>());
        Item item2FromDb = itemService.addItem(item2, user1FromBd.getId());
        item2FromDb.setComments(new ArrayList<>());
        List<Item> listItemsUser1BeforeTest = List.of(item1FromDb, item2FromDb);

        List<ItemWithBookingDto> listItemsUser1FromBd = itemService
                .getListItemsUserById(user1FromBd.getId(), 1, 10);

        assertNotNull(listItemsUser1FromBd);
        assertEquals(listItemsUser1BeforeTest.size(), listItemsUser1FromBd.size());
        assertEquals(listItemsUser1BeforeTest.get(0).getId(), listItemsUser1FromBd.get(0).getId());
        assertEquals(listItemsUser1BeforeTest.get(0).getName(), listItemsUser1FromBd.get(0).getName());
        assertEquals(listItemsUser1BeforeTest.get(1).getDescription(), listItemsUser1FromBd.get(1).getDescription());
    }

//    @DirtiesContext
//    @Test
//    void getListItemsUserById_WhenIsOwnerNotFound_ThenReturnedNotFoundException() {
//        assertThrows(NotFoundException.class,
//                () -> itemService.getListItemsUserById(777L, 1, 10));
//    }

    @DirtiesContext
    @Test
    void getSearchItems_WhenTextFromNameIsOk_ThenReturnedListItemsDto() {
        User user1FromBd = userService.addUser(user1);
        item1.setName("item1 search");
        Item item1FromDb = itemService.addItem(item1, user1FromBd.getId());
        Item item2FromDb = itemService.addItem(item2, user1FromBd.getId());
        String text = "search";
        List<Item> listItemsUser1BeforeTest = List.of(item1FromDb);

        List<ItemDto> listItemsDtoFromBd = itemService.getSearchItems(text, 1, 10);

        assertNotNull(listItemsDtoFromBd);
        assertEquals(listItemsUser1BeforeTest.size(), listItemsDtoFromBd.size());
        assertEquals(listItemsUser1BeforeTest.get(0).getId(), listItemsDtoFromBd.get(0).getId());
        assertEquals(listItemsUser1BeforeTest.get(0).getName(), listItemsDtoFromBd.get(0).getName());
        assertEquals(listItemsUser1BeforeTest.get(0).getDescription(), listItemsDtoFromBd.get(0).getDescription());
    }

    @DirtiesContext
    @Test
    void getSearchItems_WhenTextFromDescriptionIsOk_ThenReturnedListItemsDto() {
        User user1FromBd = userService.addUser(user1);
        item1.setDescription("Description search");
        Item item1FromDb = itemService.addItem(item1, user1FromBd.getId());
        Item item2FromDb = itemService.addItem(item2, user1FromBd.getId());
        String text = "search";
        List<Item> listItemsUser1BeforeTest = List.of(item1FromDb);

        List<ItemDto> listItemsDtoFromBd = itemService.getSearchItems(text, 1, 10);

        assertNotNull(listItemsDtoFromBd);
        assertEquals(listItemsUser1BeforeTest.size(), listItemsDtoFromBd.size());
        assertEquals(listItemsUser1BeforeTest.get(0).getId(), listItemsDtoFromBd.get(0).getId());
        assertEquals(listItemsUser1BeforeTest.get(0).getName(), listItemsDtoFromBd.get(0).getName());
        assertEquals(listItemsUser1BeforeTest.get(0).getDescription(), listItemsDtoFromBd.get(0).getDescription());
    }

    @DirtiesContext
    @Test
    void getSearchItems_WhenTextIsNull_ThenReturnedCollectionsIsEmpty() {
        User user1FromBd = userService.addUser(user1);
        Item item1FromDb = itemService.addItem(item1, user1FromBd.getId());
        Item item2FromDb = itemService.addItem(item2, user1FromBd.getId());
        String text = null;

        List<ItemDto> listItemsDtoFromBd = itemService.getSearchItems(text, 1, 10);

        assertNotNull(listItemsDtoFromBd);
        assertEquals(0, listItemsDtoFromBd.size());
    }

    @DirtiesContext
    @Test
    void getSearchItems_WhenTextIsBlank_ThenReturnedCollectionsIsEmpty() {
        User user1FromBd = userService.addUser(user1);
        Item item1FromDb = itemService.addItem(item1, user1FromBd.getId());
        Item item2FromDb = itemService.addItem(item2, user1FromBd.getId());
        String text = "";

        List<ItemDto> listItemsDtoFromBd = itemService.getSearchItems(text, 1, 10);

        assertNotNull(listItemsDtoFromBd);
        assertEquals(0, listItemsDtoFromBd.size());
    }

    @DirtiesContext
    @Test
    void checkingIsAvailable_WhenIsAvailableFalse_ThenReturnedValidateException() {
        User user1FromBd = userService.addUser(user1);
        Item item1FromDb = itemService.addItem(item1, user1FromBd.getId());
        item1FromDb.setAvailable(false);
        assertThrows(ValidateException.class,
                () -> itemService.checkingIsAvailable(item1FromDb));
    }

    @DirtiesContext
    @Test
    void addComment_WhenAllIsOk_ThenReturnedCommentFromBd() {
        User user1FromBd = userService.addUser(user1);
        User bookerFromBd = userService.addUser(user2);
        Item item1FromDb = itemService.addItem(item1, user1FromBd.getId());
        bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().minusHours(5))
                .end(LocalDateTime.now().minusHours(3))
                .itemId(item1FromDb.getId())
                .build();
        Booking booking = toBooking(bookingDto);
        booking.setBooker(bookerFromBd);

        Booking bookingFromBd = bookingRepository.save(booking);
        item1FromDb.setBookings(List.of(bookingFromBd));
        bookingFromBd.setItem(item1FromDb);
        bookingFromBd.setBooker(bookerFromBd);

        Comment commentFromBd = itemService.addComment(comment, bookerFromBd.getId(), item1FromDb.getId());
        List<Comment> commentsListByItemFromBd = item1FromDb.getComments();

        assertNotNull(commentFromBd);
        assertEquals(1, commentsListByItemFromBd.size());
        assertEquals(comment.getText(), commentFromBd.getText());
        assertEquals(comment.getCreated(), commentFromBd.getCreated());
    }

//    @DirtiesContext
//    @Test
//    void addComment_WhenAllItemIsNotFound_ThenReturnedCommentFromBd() {
//        assertThrows(NotFoundException.class,
//                () -> itemService.addComment(comment, user1.getId(), item1.getId()));
//    }

    @DirtiesContext
    @Test
    void findNextBookingByDate_WhenAllIsOk_ThenReturnedBooking() {
        Booking nextBooking = null;
        User bookerFromBd1 = userRepository.save(user1);
        User bookerFromBd2 = userRepository.save(user2);
        item1.setOwner(user1);
        Item itemFromBd = itemRepository.save(item1);
        Booking booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2))
                .item(itemFromBd)
                .booker(bookerFromBd1)
                .status(Status.WAITING)
                .build();
        Booking booking2 = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().plusHours(3))
                .end(LocalDateTime.now().plusHours(4))
                .item(itemFromBd)
                .booker(bookerFromBd2)
                .status(Status.WAITING)
                .build();
        Booking bookingFromBd1 = bookingRepository.save(booking1);
        Booking bookingFromBd2 = bookingRepository.save(booking2);
        itemFromBd.setBookings(new ArrayList<>(List.of(bookingFromBd1, bookingFromBd2)));

        nextBooking = itemServiceImpl.findNextBookingByDate(itemFromBd.getId());

        assertNotNull(nextBooking);
        assertEquals(bookingFromBd2.getId(), nextBooking.getId());
        assertEquals(bookingFromBd2.getItem(), nextBooking.getItem());
        assertEquals(bookingFromBd2.getStatus(), nextBooking.getStatus());
    }

    @DirtiesContext
    @Test
    void findLastBookingByDate_WhenAllIsOk_ThenReturnedBooking() {
        Booking lastBooking = null;
        User bookerFromBd1 = userRepository.save(user1);
        User bookerFromBd2 = userRepository.save(user2);
        item1.setOwner(user1);
        Item itemFromBd = itemRepository.save(item1);
        Booking booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(itemFromBd)
                .booker(bookerFromBd1)
                .status(Status.WAITING)
                .build();
        Booking booking2 = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().minusHours(3))
                .end(LocalDateTime.now().minusHours(1))
                .item(itemFromBd)
                .booker(bookerFromBd2)
                .status(Status.WAITING)
                .build();
        Booking bookingFromBd1 = bookingRepository.save(booking1);
        Booking bookingFromBd2 = bookingRepository.save(booking2);
        itemFromBd.setBookings(new ArrayList<>(List.of(bookingFromBd1, bookingFromBd2)));

        lastBooking = itemServiceImpl.findLastBookingByDate(itemFromBd.getId());

        assertNotNull(lastBooking);
        assertEquals(bookingFromBd2.getId(), lastBooking.getId());
        assertEquals(bookingFromBd2.getItem(), lastBooking.getItem());
        assertEquals(bookingFromBd2.getStatus(), lastBooking.getStatus());
    }

    @Test
    void userMapperTest_mapToUser_whenAllIsOk() {
        User user1 = toUser(userDto1);
        assertEquals(userDto1.getId(), user1.getId());
        assertEquals(userDto1.getName(), user1.getName());
        assertEquals(userDto1.getEmail(), user1.getEmail());
    }

    @Test
    void userMapperTest_mapToUserDto_whenAllIsOk() {
        UserDto userDto = toUserDto(user1);
        assertEquals(userDto.getId(), user1.getId());
        assertEquals(userDto.getName(), user1.getName());
        assertEquals(userDto.getEmail(), user1.getEmail());
    }


}