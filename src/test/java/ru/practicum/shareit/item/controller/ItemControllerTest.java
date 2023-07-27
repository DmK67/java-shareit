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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("ItemDto")
                .description("ItemDto description")
                .available(true)
                .requestId(requestId)
                .build();

        item = toItem(itemDto);

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

        owner = User.builder().id(2L).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addItem_WhenAllParamsIsOkThenReturnItem() throws Exception {
        when(itemService.addItem(any(), anyLong())).thenReturn(item);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService).addItem(item, 1L);
    }

    @Test
    void getItemById_WhenAllParamsIsOk_ThenReturnItem() throws Exception {
        when(itemService.getItemById(anyLong())).thenReturn(item);

        mockMvc.perform(get("/items/{id}", 1L)
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
    void getItemByIdWithBooking_WhenAllParamsIsOk_ThenReturnItemWithBookingDto() throws Exception {
        when(itemService.getItemByIdWithBooking(anyLong(), anyLong())).thenReturn(itemWithBookingDto);

        mockMvc.perform(get("/items/{id}", 1L)
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
    void updateItem() {
    }

    @Test
    void getItemByIdWithBooking() {
    }

    @Test
    void getListItems() {
    }

    @Test
    void getSearchItems() {
    }

    @Test
    void addComment() {
    }
}