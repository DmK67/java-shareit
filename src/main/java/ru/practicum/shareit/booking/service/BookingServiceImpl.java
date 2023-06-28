package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationService;

import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final UserService userService;

    private final ValidationService validationService;

    @Override
    public Booking addBooking(BookingDto bookingDto, Long bookerId) { // Метод добавления бронирования

        //        validateBooking(bookingDto, itemFromDB, bookerFromDb);
        //        bookingDto.setBookingStatus(BookingStatus.WAITING);
        //        Booking booking = bookingMapper.mapToModel(bookingDto);

        Item itemDB = itemService.getItemById(bookingDto.getItemId()); // Получение и проверка на наличии вещи в БД
        itemService.checkingIsAvailable(itemDB); // Проверка доступности к бронированию
        User bookerDB = userService.getUserById(bookerId); // Получение и проверка на наличии пользователя в БД
        validationService.checkBookingDtoWhenAdd(bookingDto);

        bookingDto.setStatus(Status.WAITING);
        Booking booking = toBooking(bookingDto);
        booking.setBooker(userService.getUserById(bookerId));
        booking.setItem(itemService.getItemById(bookingDto.getItemId()));
        return bookingRepository.save(booking);
        //return null;
    }

    @Override
    public Booking getBookingById(Long id) {
        return null;
    }

    @Override
    public Booking updateBooking(Booking booking, Long userId) {
        return null;
    }

    @Override
    public void deleteBooking(Long id) {

    }

    @Override
    public List<Booking> getListAllBooking() {
        return null;
    }
}
