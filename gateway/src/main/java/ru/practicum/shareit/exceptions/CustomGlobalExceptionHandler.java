package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class CustomGlobalExceptionHandler {

    @ExceptionHandler({MissingRequestHeaderException.class, MethodArgumentNotValidException.class,
            ConstraintViolationException.class, ValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomErrorResponse badRequest(final Exception e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return CustomErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomErrorResponse handleThrowable(Throwable throwable) {
        log.error("Unknown error", throwable);
        return CustomErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(throwable.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }
}
