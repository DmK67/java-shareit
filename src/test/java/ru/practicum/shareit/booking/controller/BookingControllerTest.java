package ru.practicum.shareit.booking.controller;


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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;


@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BookingService bookingService;
    @MockBean
    ItemRepository itemRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    BookingRepository bookingRepository;
    @Autowired
    MockMvc mockMvc;

    BookingDto bookingDtoForAdd;
    User owner1;
    User booker;
    Item item1;

    @BeforeEach
    void setUp() {

        booker = User.builder()
                .id(10L)
                .name("booker")
                .email("booker@ya.ru")
                .build();

        item1 = Item.builder()
                .id(1L)
                .name("item1")
                .description("description")
                .owner(owner1)
                .available(true)
                .build();

        bookingDtoForAdd = BookingDto.builder()
                .id(1L)
                .itemId(1L)
                .booker(toUserDto(booker))
                .start(LocalDateTime.now().plusHours(10))
                .end(LocalDateTime.now().plusHours(20))
                .item(toItemDto(item1))
                .status(Status.WAITING)
                .build();

        owner1 = User.builder()
                .id(1L)
                .name("owner1")
                .email("owner1@ya.ru")
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addBooking_WhenAllIsOk_ReturnBookingDto() throws Exception {
        BookingDto bookingDtoForResponse = BookingDto.builder()
                .id(1L)
                .start(bookingDtoForAdd.getStart())
                .end(bookingDtoForAdd.getEnd())
                .itemId(item1.getId())
                .booker(toUserDto(booker))
                .item(toItemDto(Item.builder().id(item1.getId()).build()))
                .status(Status.WAITING).build();
        Booking booking = toBooking(bookingDtoForResponse);
        booking.setBooker(booker);

        when(bookingService.addBooking(any(), any())).thenReturn(toBookingDto(booking));

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoForAdd)))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoForResponse), result);
    }

    @Test
    void addBooking_WhenEndTimeBeforeStartTime_ReturnedValidateException() throws Exception {
        bookingDtoForAdd.setStart(LocalDateTime.now().minusHours(10));
        bookingDtoForAdd.setEnd(LocalDateTime.now().plusHours(20));

        when(bookingService.addBooking(any(), any())).thenThrow(ValidateException.class);

        assertThrows(ValidateException.class, () -> bookingService.addBooking(bookingDtoForAdd, booker.getId()));
    }


    @Test
    void updateBooking_WhenAllOk_ReturnBookingDto() throws Exception {
        BookingDto bookingDtoForResponse = BookingDto.builder()
                .id(1L)
                .start(bookingDtoForAdd.getStart())
                .end(bookingDtoForAdd.getEnd())
                .itemId(item1.getId())
                .booker(toUserDto(booker))
                .item(toItemDto(Item.builder().id(item1.getId()).build()))
                .status(Status.WAITING).build();
        Booking booking = toBooking(bookingDtoForResponse);
        booking.setBooker(booker);

        when(bookingService.updateBooking(any(), any(), any()))
                .thenReturn(bookingDto);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingDtoForResponse.getId())
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", owner1.getId())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoForResponse), result);
    }


    @Test
    void getBookingByIdAndStatus_WhenAllOk_ReturnBooking() throws Exception {
        BookingDto bookingDtoForResponse = BookingDto.builder()
                .id(1L)
                .start(bookingDtoForAdd.getStart())
                .end(bookingDtoForAdd.getEnd())
                .itemId(item1.getId())
                .booker(toUserDto(booker))
                .item(toItemDto(Item.builder().id(item1.getId()).build()))
                .status(Status.WAITING).build();
        Booking booking = toBooking(bookingDtoForResponse);
        booking.setBooker(booker);

        when(bookingService.getBookingByIdAndStatus(any(), any())).thenReturn(booking);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}",
                                bookingDtoForResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId()))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(bookingDtoForResponse), result);
    }

    @Test
    void getListBookingsUserById_WhenAllOk_ReturnListBooking() throws Exception {
        BookingDto bookingDtoForResponse = BookingDto.builder()
                .id(1L)
                .start(bookingDtoForAdd.getStart())
                .end(bookingDtoForAdd.getEnd())
                .booker(toUserDto(booker))
                .item(toItemDto(Item.builder().id(item1.getId()).build()))
                .status(Status.WAITING).build();
        Booking booking = toBooking(bookingDtoForResponse);
        booking.setBooker(booker);

        when(bookingService.getListBookingsUserById(any(), any(), any(), any())).thenReturn(List.of(booking));

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(booking)), result);
    }

    @Test
    void getListBookingsOwnerById() throws Exception {
        BookingDto bookingDtoForResponse = BookingDto.builder()
                .id(1L)
                .start(bookingDtoForAdd.getStart())
                .end(bookingDtoForAdd.getEnd())
                .booker(toUserDto(booker))
                .item(toItemDto(Item.builder().id(item1.getId()).build()))
                .status(Status.WAITING).build();
        Booking booking = toBooking(bookingDtoForResponse);
        booking.setBooker(booker);

        when(bookingService.getListBookingsUserById(any(), any(), any(), any())).thenReturn(List.of(booking));

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(booking)), result);
    }

    @Test
    void getListBookingsOwnerById_WhenAllOk_ReturnListBooking() throws Exception {
        BookingDto bookingDtoForResponse = BookingDto.builder()
                .id(1L)
                .start(bookingDtoForAdd.getStart())
                .end(bookingDtoForAdd.getEnd())
                .booker(toUserDto(booker))
                .item(toItemDto(Item.builder().id(item1.getId()).build()))
                .status(Status.WAITING).build();
        Booking booking = toBooking(bookingDtoForResponse);
        booking.setBooker(booker);

        when(bookingService.getListBookingsOwnerById(any(), any(), any(), any()))
                .thenReturn(List.of(booking));

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", owner1.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(booking)), result);
    }
}