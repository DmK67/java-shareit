package ru.practicum.shareit.itemRequest.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.itemRequest.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.validation.ValidationService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.itemRequest.mapper.ItemRequestMapper.toItemRequest;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceTest {
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private BookingServiceImpl bookingService;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    ItemService itemService;
    @Mock
    private ValidationService validationService;
    private final EntityManager entityManager;

    TypedQuery<ItemRequest> query;
    UserDto ownerDto;
    UserDto requesterDto;
    ItemRequest itemRequest;
    ItemRequestDto itemRequestDto;
    Item item;

    @BeforeEach
    void setUp() {
        ownerDto = UserDto.builder()
                .id(1L)
                .name("OwnerDto")
                .email("OwnerDto@ya.ru")
                .build();

        requesterDto = UserDto.builder()
                .id(1L)
                .name("RequesterDto")
                .email("requesterDto@ya.ru")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item")
                .description("description item")
                .owner(toUser(ownerDto))
                .available(true)
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description(item.getDescription())
                .requestor(toUser(requesterDto))
                .created(null)
                .build();
        itemRequest =toItemRequest(itemRequestDto);
        itemRequest.setItems(null);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addItemRequest() {
        when(userService.getUserById(anyLong())).thenReturn(toUser(ownerDto));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        ItemRequest itemRequestFromBd = itemRequestService.addItemRequest(itemRequest, 1L);

        assertEquals(itemRequestFromBd, itemRequest);
    }

    @Test
    void getItemRequestsByUserId() {
    }

    @Test
    void getListRequestsCreatedByOtherUsers() {
    }

    @Test
    void getItemRequestById() {
    }
}