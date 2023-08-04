package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StateDto;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exceptions.StateStatusValidateException;
import ru.practicum.shareit.exceptions.ValidateException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /**
     * • Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем,
     * а затем подтверждён владельцем вещи. Эндпоинт — POST /bookings.
     * После создания запрос находится в статусе WAITING — «ожидает подтверждения».
     */
    public ResponseEntity<Object> addBooking(BookingDto requestDto, long bookerId) {
        checkBookingDtoWhenAdd(requestDto);// Проверка полей объекта BookingDto перед добавлением
        return post("", bookerId, requestDto);
    }


    /**
     * • Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
     * Затем статус бронирования становится либо APPROVED, либо REJECTED.
     * Эндпоинт — PATCH /bookings/{bookingId}?approved={approved}, параметр approved может принимать
     * значения true или false.
     *
     * @param ownerId   ID владельца вещи.
     * @param bookingId ID брони.
     * @param approved  True - подтверждено, False - отклонено.
     * @return Обновленная бронь.
     */
    public ResponseEntity<Object> updateBooking(Long ownerId, Boolean approved, Long bookingId) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", ownerId, parameters, null);
    }

    /**
     * • Получение данных о конкретном бронировании (включая его статус).
     * Может быть выполнено либо автором бронирования, либо владельцем вещи,
     * к которой относится бронирование.
     * Эндпоинт — GET /bookings/{bookingId}.
     */
    public ResponseEntity<Object> getBookingByIdAndStatus(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    /**
     * • Получение списка всех бронирований текущего пользователя.
     * Эндпоинт — GET /bookings?state={state}.
     * Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     * Также он может принимать значения CURRENT (англ. «текущие»), PAST (англ. «завершённые»),
     * FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
     * Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
     */
    public ResponseEntity<Object> getListBookingsUserById(long userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        checkStatusState(state); // Проверка statusState
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }


    /**
     * • Получение списка бронирований для всех вещей текущего пользователя.
     * Эндпоинт — GET /bookings/owner?state={state}.
     * Этот запрос имеет смысл для владельца хотя бы одной вещи.
     * Работа параметра state аналогична его работе в предыдущем сценарии.
     */
    public ResponseEntity<Object> getListBookingsOwnerById(long userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        checkStatusState(state); // Проверка stateDto
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    private void checkBookingDtoWhenAdd(BookingDto bookingDto) {
        // Метод проверки полей объекта BookingDto перед добавлением
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            log.error("Ошибка! Поля начала и окончания бронирования не могут быть пустыми!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            log.error("Ошибка! Дата и время окончания бронирования не может предшествовать дате и времени начала " +
                    "бронирования!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            log.error("Ошибка! Дата и время начала бронирования равно дате и времени окончания бронирования!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            log.error("Ошибка! Указано неправильно дата/время начала бронирования!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            log.error("Ошибка! Указано неправильно дата/время окончания бронирования!");
            throw new ValidateException("Ошибка! Указано неправильно дата или время бронирования!");
        }
    }

    private void checkStatusState(String state) {
        if (state.equalsIgnoreCase(StateDto.ALL.name())) {
            return;
        }
        if (state.equalsIgnoreCase(StateDto.CURRENT.name())) {
            return;
        }
        if (state.equalsIgnoreCase(StateDto.PAST.name())) {
            return;
        }
        if (state.equalsIgnoreCase(StateDto.FUTURE.name())) {
            return;
        }
        if (state.equalsIgnoreCase(StateDto.WAITING.name())) {
            return;
        }
        if (state.equalsIgnoreCase(StateDto.REJECTED.name())) {
            return;
        }
        log.error("Ошибка! Указан неправильно статус бронирования!");
        throw new StateStatusValidateException();
    }
}