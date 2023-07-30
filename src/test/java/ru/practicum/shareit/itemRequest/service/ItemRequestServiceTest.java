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
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.itemRequest.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utility.ValidationClass;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.itemRequest.mapper.ItemRequestMapper.*;
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
    private ValidationClass validationService;
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
        itemRequest = toItemRequest(itemRequestDto);
        itemRequest.setItems(null);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addItemRequest_WhenAllIsOk_ThenReturnItemRequest() {
        when(userService.getUserById(anyLong())).thenReturn(toUser(ownerDto));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        ItemRequest itemRequestFromBd = itemRequestService.addItemRequest(itemRequest, 1L);

        assertEquals(itemRequestFromBd, itemRequest);
    }

    @Test
    void addItemRequest_WhenRequesterIsNull_ReturnValidateException() {
        Long requesterId = null;
        assertThrows(ValidateException.class,
                () -> itemRequestService.addItemRequest(itemRequest, requesterId));
    }

    @Test
    void getItemRequestsByUserId() {
        when(userService.getUserById(anyLong())).thenReturn(toUser(ownerDto));
        when(itemRequestRepository.getAllByRequestorIdOrderByCreatedDesc(anyLong())).thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(new ArrayList<>());

        assertEquals(itemRequestService.getItemRequestsByUserId(1L),
                List.of(toItemRequestDtoWithAnswers(itemRequest)));

    }

    @Test
    void getListRequestsCreatedByOtherUsers_WhenAllIsOk_ThenReturnListItemRequestDtoWithAnswers() {
        when(userService.getUserById(anyLong())).thenReturn(toUser(ownerDto));
        when(itemRequestRepository.findAll()).thenReturn(new ArrayList<>());
        when(itemRequestRepository.getItemRequestByRequesterIdIsNotOrderByCreated(anyLong(), any()))
                .thenReturn(List.of(itemRequest));

        assertEquals(itemRequestService.getListRequestsCreatedByOtherUsers(1L, 1, 1),
                List.of(toItemRequestDtoWithAnswers(itemRequest)));
    }

    @Test
    void getItemRequestById_WhenAllIsOk_ThenReturnItemRequestDtoWithAnswers() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(toUser(ownerDto)));

        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemRequest));

        when(itemRequestRepository.findAll()).thenReturn(new ArrayList<>());

        assertEquals(itemRequestService.getItemRequestById(1L, 1L), toItemRequestDtoWithAnswers(itemRequest));
    }

    @Test
    void getItemRequestById_WhenItemRequestNotFound_ThenReturnNotFoundException() {
        when(itemRequestRepository.findById(anyLong()))
                .thenThrow(new NotFoundException(String.format("Запрос на вещь по id=%d не существует!", 1L)));

        Exception e = assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequestById(1L, 1L));

        assertEquals(e.getMessage(), String.format("Запрос на вещь по id=%d не существует!", 1L));
    }

    @Test
    void getItemRequestById_WhenItemRequestIdIsNull_ThenReturnValidateException() {
        assertThrows(ValidateException.class,
                () -> itemRequestService.getItemRequestById(1L, null));
    }

    @Test
    void itemRequestMapperTest_toItemRequestDtoWithAnswers_WhenAllIsOk() {
        ItemRequestDtoWithAnswers itemRequestDtoWithAnswers = toItemRequestDtoWithAnswers(itemRequest);
        assertEquals(itemRequestDtoWithAnswers.getId(), itemRequest.getId());
        assertEquals(itemRequestDtoWithAnswers.getDescription(), itemRequest.getDescription());
    }

    @Test
    void itemRequestMapperTest_toItemRequestDto_WhenAllIsOk() {
        ItemRequestDto itemRequestDto = toItemRequestDto(itemRequest);
        assertEquals(itemRequestDto.getId(), itemRequest.getId());
        assertEquals(itemRequestDto.getDescription(), itemRequest.getDescription());
    }
}