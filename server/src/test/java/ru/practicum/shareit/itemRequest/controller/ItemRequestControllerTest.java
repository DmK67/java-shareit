package ru.practicum.shareit.itemRequest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.itemRequest.service.ItemRequestService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.itemRequest.mapper.ItemRequestMapper.toItemRequest;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ItemRequestService itemRequestService;
    @Autowired
    MockMvc mockMvc;

    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("testDescription")
            .build();
    private final ItemRequestDtoWithAnswers itemRequestDtoWithAnswers = ItemRequestDtoWithAnswers.builder()
            .id(1L)
            .description("testDescription")
            .build();

    @Test
    void addItemRequest_WhenAllIsOk_ThenReturnItemRequestDto() throws Exception {
        when(itemRequestService.addItemRequest(any(), anyLong()))
                .thenReturn(toItemRequest(itemRequestDto));

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }

    @Test
    void getItemRequestsByUserId_WhenAllIsOk_ListItemRequestDtoWithAnswers() throws Exception {
        when(itemRequestService.getItemRequestsByUserId(anyLong()))
                .thenReturn(Collections.emptyList());

        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, objectMapper.writeValueAsString(Collections.emptyList()));
    }

    @Test
    void getAllRequests_WhenAllIsOk_ThanReturnListItemRequestDtoWithAnswers() throws Exception {
        when(itemRequestService.getListRequestsCreatedByOtherUsers(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestDtoWithAnswers));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService).getListRequestsCreatedByOtherUsers(1L, 0, 20);
    }


    @Test
    void getItemRequestById_WhenAllIsOk_ThanReturnListItemRequestDtoWithAnswers() throws Exception {
        when(itemRequestService.getItemRequestById(any(), anyLong()))
                .thenReturn(itemRequestDtoWithAnswers);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService).getItemRequestById(1L, 1L);
    }
}