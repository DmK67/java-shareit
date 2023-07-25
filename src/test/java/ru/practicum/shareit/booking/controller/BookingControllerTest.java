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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserForResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;
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

    /**
     * mapper
     */
//    @Spy        //Для интерфейса используем Spy.
//    ItemForResponseDtoMapper itemForResponseDtoMapper;
    /**
     * mapper
     */
//    @Spy
//    UserToUserOnlyWithIdDtoMapper userOnlyWithIdDtoMapper;
    /**
     * DTO-объект для создания бронирования №1.
     */
    BookingDto bookingDtoForAdd;
    /**
     * Хозяин №1 вещи №1.
     */
    User owner1;
    /**
     * Букер №1 вещи №1.
     */
    User booker1;
    /**
     * Вещь №1.
     */
    Item item1;
    LocalDateTime now;
    LocalDateTime nowPlus10Hours;
    LocalDateTime nowPlus20Hours;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        nowPlus10Hours = LocalDateTime.now().plusHours(10);
        nowPlus20Hours = LocalDateTime.now().plusHours(20);

        booker1 = User.builder()
                .id(10L)
                .name("booker1")
                .email("booker1@ya.ru")
                .build();

        bookingDtoForAdd = BookingDto.builder()
                .id(1L)
                .itemId(1L)
                .booker(UserDto.builder().id(booker1.getId()).name(booker1.getName()).build())
                .start(nowPlus10Hours)
                .end(nowPlus20Hours)
                .status(Status.WAITING)
                .build();

        owner1 = User.builder()
                .id(1L)
                .name("owner1")
                .email("owner1@ya.ru")
                .build();

        item1 = Item.builder()
                .id(1L)
                .name("item1")
                .description("description")
                .owner(owner1)
                .available(true)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addBooking_WhenAllIsOk_ReturnBookingDto() throws Exception { // продолжаем
        BookingDto bookingDto1ForResponse = BookingDto.builder()
                .id(1L)
                .start(bookingDtoForAdd.getStart())
                .end(bookingDtoForAdd.getEnd())
                .itemId(item1.getId())
                .booker(toUserDto(booker1))
                .status(Status.WAITING).build();

        when(bookingService.addBooking(any(), any())).thenReturn(toBooking(bookingDto1ForResponse));

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booker1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoForAdd)))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto1ForResponse), result);
    }

    @Test
    void updateBooking() {
    }

    @Test
    void getBookingByIdAndStatus() {
    }

    @Test
    void getListBookingsUserById() {
    }

    @Test
    void getListBookingsOwnerById() {
    }
}