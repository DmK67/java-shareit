package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.comment.mapper.CommentMapper.toComment;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.item.mapper.ItemWithBookingDtoMapper.toItemWithBooking;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ItemService itemService;

    @Autowired
    MockMvc mockMvc;

    Item item;
    ItemDto itemDto;

    ItemWithBooking itemWithBooking;
    ItemWithBookingDto itemWithBookingDto;
    Long requestId;
    User owner;
    BookingForItemDto lastBooking;
    BookingForItemDto nextBooking;
    CommentDto commentDto;
    List<ItemWithBookingDto> itemWithBookingDtoList;


    @BeforeEach
    void setUp() {
        owner = User.builder().id(2L).build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("ItemDto")
                .description("ItemDto description")
                .available(true)
                .requestId(requestId)
                .build();

        item = toItem(itemDto);
        item.setOwner(owner);

        lastBooking = BookingForItemDto.builder()
                .id(1L)
                .startTime(LocalDateTime.now().minusDays(2))
                .endTime(LocalDateTime.now().minusDays(1))
                .build();

        nextBooking = BookingForItemDto.builder()
                .id(2L)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .build();

        itemWithBookingDto = ItemWithBookingDto.builder()
                .id(2L)
                .name("ItemWithBookingDto")
                .description("ItemWithBookingDto description")
                .available(true)
                .requestId(requestId)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();

        itemWithBooking = toItemWithBooking(itemWithBookingDto);

        itemWithBookingDtoList = List.of(
                new ItemWithBookingDto(1L, "Name1", "Description1", true, null,
                        null, null, null), itemWithBookingDto);

        commentDto = CommentDto.builder().id(1L).item(item).text("Text").authorName("Name").build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addItem_WhenAllIsOkThenReturnItem() throws Exception {
        when(itemService.addItem(any(), anyLong())).thenReturn(toItem(itemDto));

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toItem(itemDto))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService).addItem(toItem(itemDto), 1L);
    }

    @Test
    void getItemById_WhenAllIsOk_ThenReturnItem() throws Exception {
        when(itemService.getItemById(anyLong())).thenReturn(item);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, never()).getItemById(1L);
    }

    @Test
    void getItemByIdWithBookingDto_WhenAllIsOk_ThenReturnItemWithBookingDto() throws Exception {
        when(itemService.getItemByIdWithBooking(anyLong(), anyLong())).thenReturn(itemWithBookingDto);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemWithBooking)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, never()).getItemByIdWithBooking(1L, owner.getId());
    }

    @Test
    void updateItem_WhenAllIsOk_ThenReturnItem() throws Exception {
        ItemDto updateItemDto = ItemDto.builder()
                .id(1L)
                .name("updateItem")
                .description("Description updateItem")
                .available(true)
                .requestId(1L)
                .build();

        when(itemService.updateItem(any(), anyLong(), anyLong()))
                .thenReturn(toItem(updateItemDto));

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(updateItemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService).updateItem(toItem(updateItemDto), 1L, 1L);
    }

    @Test
    void getListItems_WhenAllIaOk_ThenReturnListItemWithBookingDto() throws Exception {
        when(itemService.getListItemsUserById(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemWithBookingDtoList);

        String result = mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, objectMapper.writeValueAsString(itemWithBookingDtoList));

    }

    @Test
    void getSearchItems_WhenAllIaOk_ThenReturnListItemDto() throws Exception {
        when(itemService.getSearchItems(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search?text={text}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("ItemDto")))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder("ItemDto description")));
    }

    @Test
    void addComment_WhenAllIsOkThenReturnCommentDto() throws Exception {
            when(itemService.addComment(any(), anyLong(), any()))
                    .thenReturn(toComment(commentDto));

            mockMvc.perform(post("/items/1/comment")
                            .header("X-Sharer-User-Id", 1L)
                            .content(objectMapper.writeValueAsString(commentDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(commentDto.getId()))
                    .andExpect(jsonPath("$.text").value(commentDto.getText()))
                    .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()));
    }
}