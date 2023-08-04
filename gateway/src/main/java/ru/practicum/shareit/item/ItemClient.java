package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
@Slf4j
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(ItemDto itemDto, Long ownerId) {
        checkItemDtoWhenAdd(itemDto); // Проверяем поля объекта itemDto перед добавлением
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> updateItem(ItemDto itemDto, long itemId, long ownerId) {
        return patch("/" + itemId, ownerId, itemDto);
    }

    public ResponseEntity<Object> getListItemsUserById(Long ownerId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> getItemByIdWithBooking(Long itemId, Long ownerId) {
        return get("/" + itemId, ownerId);
    }

    public ResponseEntity<Object> getSearchItems(Long userId, String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(Long itemId, Long userId, CommentDto commentDto) {
        checkCommentText(commentDto.getText());
        return post("/" + itemId + "/comment", userId, commentDto);
    }
    private void checkItemDtoWhenAdd(ItemDto itemDto) { // Метод проверки полей объекта ItemDto перед добавлением
        if (itemDto.getAvailable() == null
                || itemDto.getName() == null || itemDto.getName().isBlank()
                || itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            log.error("Ошибка! Вещь с пустыми полями не может быть добавлена!");
            throw new ValidateException("Ошибка! Вещь с пустыми полями не может быть добавлена!");
        }
    }

    public void checkCommentText(String text) { // Метод проверки поля text
        if (text == null || text.isBlank()) {
            throw new ValidateException("Текст комментария не может быть пустым.");
        }
    }
}