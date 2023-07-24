package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    ItemRepository itemRepository;

    private final User user1 = new User(1L, "User1", "user1@email.com");
    private final Item item = Item.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(user1)
            .build();

    @Test
    void addItem() {
    }

    @Test
    void getItemById_WhenUserIsNotFound_ThenReturnedNotFoundException() {

        Exception e = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(1L));

        assertEquals(e.getMessage(), String.format("Вещь по id=" + item.getId() + " не существует!", 1L));
    }

    @Test
    void getItemByIdWithBooking() {
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