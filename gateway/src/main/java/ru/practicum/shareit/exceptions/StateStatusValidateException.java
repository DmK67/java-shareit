package ru.practicum.shareit.exceptions;

public class StateStatusValidateException extends RuntimeException {
    public StateStatusValidateException() {
        super("Unknown state: UNSUPPORTED_STATUS");
    }
}
