package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
Не используется.
Все проверки совершаются благодаря аннотациям.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}
