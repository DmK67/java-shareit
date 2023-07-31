package ru.practicum.shareit.itemRequest.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.itemRequest.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.itemRequest.mapper.ItemRequestMapper.*;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class ItemRequestServiceIntegrationTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemRequestService itemRequestService;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    private final EntityManager entityManager;

    UserDto ownerDto;
    User owner;
    UserDto requesterDto;
    User requester;
    ItemRequest itemRequest;
    ItemRequestDto itemRequestDto;
    Item item;


    @BeforeEach
    void setUp() {

        itemRequestService = new ItemRequestServiceImpl(userRepository,
                itemRequestRepository, itemRepository);

        ownerDto = UserDto.builder()
                .id(1L)
                .name("OwnerDto")
                .email("OwnerDto@ya.ru")
                .build();

        owner = toUser(ownerDto);

        requesterDto = UserDto.builder()
                .id(2L)
                .name("RequesterDto")
                .email("requesterDto@ya.ru")
                .build();

        requester = toUser(requesterDto);

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
    }

    @Test
    void addItemRequest_WhenAllIsOk_ThenReturnItemRequest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(requester));
        when(itemRequestService.addItemRequest(itemRequest, requester.getId())).thenReturn(itemRequest);

        ItemRequest itemRequestFromBd = itemRequestService.addItemRequest(itemRequest, 1L);

        assertEquals(itemRequestFromBd, itemRequest);
    }

    @Test
    void addItemRequest_WhenRequesterIsNull_ReturnValidateException() {
        assertThrows(ValidateException.class,
                () -> itemRequestService.addItemRequest(itemRequest, null));
    }

    @Test
    void addItemRequest_WhenRequesterNotFound_ReturnNotFoundException() {
        Long requesterId = 1001L;
        assertThrows(NotFoundException.class,
                () -> itemRequestService.addItemRequest(itemRequest, requesterId));
    }

    @Test
    void getItemRequestsByUserId_WhenAllIsOk_ThenReturnListItemRequest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(requester));
        when(itemRequestRepository.getAllByRequestorIdOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDtoWithAnswers> result = itemRequestService.getItemRequestsByUserId(2L);

        assertEquals(1, result.size());
        assertEquals(itemRequest.getId(), result.get(0).getId());
        assertEquals(itemRequest.getDescription(), result.get(0).getDescription());
    }


    @Test
    void getListRequestsCreatedByOtherUsers_WhenAllIsOk_ThenReturnListItemRequestDtoWithAnswers() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(requester));
        when(itemRequestRepository.getItemRequestByRequesterIdIsNotOrderByCreated(anyLong(), any()))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDtoWithAnswers> result =
                itemRequestService.getListRequestsCreatedByOtherUsers(2L, 1, 1);

        assertEquals(1, result.size());
        assertEquals(itemRequest.getId(), result.get(0).getId());
        assertEquals(itemRequest.getDescription(), result.get(0).getDescription());
    }

    @Test
    void getItemRequestById_WhenAllIsOk_ThenReturnItemRequestDtoWithAnswers() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(toUser(ownerDto)));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemRequest));

        ItemRequestDtoWithAnswers itemRequestDtoWithAnswersFromBd =
                itemRequestService.getItemRequestById(1L, 1L);

        assertEquals(itemRequest.getDescription(), itemRequestDtoWithAnswersFromBd.getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestDtoWithAnswersFromBd.getCreated());
    }

    @Test
    void getItemRequestById_WhenItemRequestNotFound_ThenReturnNotFoundException() {

        assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequestById(1L, 1L));
    }

    @Test
    void getItemRequestById_WhenItemRequestIdIsNull_ThenReturnValidateException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(requester));

        assertThrows(ValidateException.class,
                () -> itemRequestService.getItemRequestById(requester.getId(), null));
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