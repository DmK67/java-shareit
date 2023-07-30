package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.utility.ValidationClass;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private ItemServiceImpl itemService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    private ValidationClass validationService;

    User user;
    User booker;
    User owner;
    Booking booking;
    BookingDto bookingDto;
    Item item;


    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@ya.ru")
                .build();

        booker = User.builder().id(2L).name("Booker").email("booker@ya.ru").build();

        owner = User.builder().id(3L).name("Owner").email("owner@ya.ru").build();

        item = Item.builder().id(1L).owner(owner).build();

        bookingDto = BookingDto.builder().id(1L).start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(3)).item(toItemDto(item)).booker(toUserDto(booker))
                .status(Status.WAITING).build();
        booking = toBooking(bookingDto);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addBooking_WhenAllIsOk_ThenReturnBooking() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(toBooking(bookingDto));
        BookingDto savedBookingFromBd = bookingService.addBooking(bookingDto, booker.getId());

        assertNotNull(savedBookingFromBd);
        assertEquals(savedBookingFromBd.getStart(), savedBookingFromBd.getStart());
        assertEquals(bookingDto.getEnd(), savedBookingFromBd.getEnd());
        assertEquals(bookingDto.getItemId(), savedBookingFromBd.getItem().getId());
        assertEquals(bookingDto.getStart(), savedBookingFromBd.getStart());
        assertEquals(bookingDto.getEnd(), savedBookingFromBd.getEnd());
    }

    @Test
    void getBookingById_WhenAllIsOk_ReturnBooking() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        BookingDto bookingFromBd = bookingService.getBookingById(booking.getId());

        assertEquals(booking.getId(), bookingFromBd.getId());
        assertEquals(booking.getItem().getName(), bookingFromBd.getItem().getName());
        assertEquals(booking.getStart(), bookingFromBd.getStart());
        assertEquals(booking.getEnd(), bookingFromBd.getEnd());
        assertEquals(booking.getStatus(), bookingFromBd.getStatus());
    }

    @Test
    void getBookingById_WhenBookingNotFound_ReturnBooking() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1000L));
    }

    @Test
    void getListBookingsUserById_WhenStateIsAll_ReturnListBookings() {
        booking.setBooker(booker);
        item.setBookings(List.of(booking));
        booking.setItem(item);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(any(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsUserById(user.getId(), "ALL", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getListBookingsUserById_WhenStateIsCurrent_ReturnListBookings() {
        booking.setBooker(booker);
        item.setBookings(List.of(booking));
        booking.setItem(item);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStateCurrent(any(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsUserById(user.getId(), "CURRENT", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getListBookingsUserById_WhenStateIsPast_ReturnListBookings() {
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusHours(10));
        booking.setEnd(LocalDateTime.now().minusHours(9));
        booking.setStatus(Status.APPROVED);
        booking.setItem(item);
        item.setBookings(List.of(booking));

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStatePast(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsUserById(user.getId(), "PAST", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getListBookingsUserById_WhenStateIsFuture_ReturnListBookings() {
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusHours(9));
        booking.setEnd(LocalDateTime.now().minusHours(10));
        booking.setStatus(Status.APPROVED);
        item.setBookings(List.of(booking));

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStateFuture(anyLong(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsUserById(user.getId(), "FUTURE", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getListBookingsUserById_WhenStateIsWaiting_ReturnListBookings() {
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusHours(1));
        booking.setEnd(LocalDateTime.now().minusHours(2));
        booking.setStatus(Status.WAITING);
        item.setBookings(List.of(booking));

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsUserById(user.getId(), "WAITING", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getListBookingsUserById_WhenStateIsREJECTED_ReturnListBookings() {
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusHours(1));
        booking.setEnd(LocalDateTime.now().minusHours(2));
        booking.setStatus(Status.REJECTED);
        item.setBookings(List.of(booking));

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsUserById(user.getId(), "REJECTED", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getBookingByIdAndStatus_WhenAllIsOk_ReturnBooking() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        BookingDto bookingFromBd = bookingService.getBookingByIdAndStatus(owner.getId(), booking.getId());

        assertEquals(booking.getId(), bookingFromBd.getId());
        assertEquals(booking.getItem().getName(), bookingFromBd.getItem().getName());
        assertEquals(booking.getStart(), bookingFromBd.getStart());
        assertEquals(booking.getEnd(), bookingFromBd.getEnd());
        assertEquals(booking.getStatus(), bookingFromBd.getStatus());
    }

    @Test
    void updateBooking_WhenAllIsOk_returnUpdateBooking() {
        Booking updatedBooking = booking;

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.save(any())).thenReturn(updatedBooking);

        BookingDto updatedBookingFromBd = bookingService.updateBooking(owner.getId(), true, booking.getId());

        assertNotNull(updatedBookingFromBd);
        assertEquals(bookingDto.getStart(), updatedBookingFromBd.getStart());
        assertEquals(bookingDto.getEnd(), updatedBookingFromBd.getEnd());
        assertEquals(bookingDto.getItemId(), updatedBookingFromBd.getItem().getId());
    }

    @Test
    void updateBooking_WhenStatusIsApproved_ReturnValidateException() {
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));
        ValidateException ex =
                assertThrows(ValidateException.class,
                        () -> bookingService.updateBooking(owner.getId(), true, booking.getId()));
        assertEquals("Статус изменить не возможно.", ex.getMessage());
    }

    @Test
    void getListBookingsOwnerById_WhenStateIsAll_ReturnAllBookings() {
        booking.setBooker(booker);
        item.setBookings(List.of(booking));
        booking.setItem(item);
        when(userRepository.findById(any())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsOwnerById(owner.getId(), "ALL", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getListBookingsOwnerById_WhenStateIsCurrent_ReturnAllBookings() {
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusHours(1));
        booking.setEnd(LocalDateTime.now().minusHours(1));
        booking.setStatus(Status.APPROVED);
        booking.setItem(item);
        item.setBookings(List.of(booking));

        when(userRepository.findById(any())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerAndStateCurrent(anyLong(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsOwnerById(owner.getId(), "CURRENT", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getListBookingsOwnerById_WhenStateIsPast_ReturnAllBookings() {
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusHours(9));
        booking.setEnd(LocalDateTime.now().minusHours(10));
        booking.setStatus(Status.APPROVED);
        booking.setItem(item);
        item.setBookings(List.of(booking));

        when(userRepository.findById(any())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStatePast(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsOwnerById(owner.getId(), "PAST", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getListBookingsOwnerById_WhenStateIsFuture_ReturnAllBookings() {
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusHours(9));
        booking.setEnd(LocalDateTime.now().minusHours(10));
        booking.setStatus(Status.APPROVED);
        booking.setItem(item);
        item.setBookings(List.of(booking));

        when(userRepository.findById(any())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStateFuture(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsOwnerById(owner.getId(), "FUTURE", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getListBookingsOwnerById_WhenStateIsWAITING_ReturnAllBookings() {
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusHours(9));
        booking.setEnd(LocalDateTime.now().minusHours(10));
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        item.setBookings(List.of(booking));

        when(userRepository.findById(any())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsOwnerById(owner.getId(), "WAITING", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getListBookingsOwnerById_WhenStateIsREJECTED_ReturnAllBookings() {
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusHours(9));
        booking.setEnd(LocalDateTime.now().minusHours(10));
        booking.setStatus(Status.REJECTED);
        booking.setItem(item);
        item.setBookings(List.of(booking));

        when(userRepository.findById(any())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getListBookingsOwnerById(owner.getId(), "REJECTED", 0, 5);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }
}