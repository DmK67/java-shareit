package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingRepositoryTest {


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerAndStateCurrent() {
    }

    @Test
    void findAllByItemOwnerIdAndStatePast() {
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndStateFuture() {
    }

    @Test
    void findAllByBookerIdAndStateCurrent() {
    }

    @Test
    void findAllByBookerIdAndStatePast() {
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerIdAndStateFuture() {
    }

    @Test
    void findNextBookingByDate() {
    }

    @Test
    void findLastBookingByDate() {
    }
}