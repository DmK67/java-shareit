package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Unknown state: UNSUPPORTED_STATUS")
public class StateStatusValidateException extends RuntimeException {
    public StateStatusValidateException() {
        super("Unknown state: UNSUPPORTED_STATUS");
    }
}
