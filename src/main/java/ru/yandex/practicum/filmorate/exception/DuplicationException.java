package ru.yandex.practicum.filmorate.exception;

public class DuplicationException extends RuntimeException {
    public DuplicationException(String message) {
        super(message);
    }
}