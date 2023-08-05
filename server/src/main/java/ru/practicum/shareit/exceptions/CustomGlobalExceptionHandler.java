package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class CustomGlobalExceptionHandler {
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomErrorResponse handlerNotFoundException(final RuntimeException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return CustomErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler({EmailDuplicateException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomErrorResponse handlerEmailDuplicateException(final RuntimeException e) {
        log.debug("Получен статус 409 Conflict {}", e.getMessage(), e);
        return CustomErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(e.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();
    }

    @ExceptionHandler({MissingRequestHeaderException.class, MethodArgumentNotValidException.class,
            ValidateException.class, CommentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomErrorResponse badRequest(final Exception e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return CustomErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(e.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomErrorResponse statusError(final Throwable e) {
        log.debug("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return CustomErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomErrorResponse internalServerError(final Throwable e) {
        log.debug("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return CustomErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }
}
