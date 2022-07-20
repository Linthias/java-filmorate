package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
Исключение, используемое при проверке даты выхода фильма.
Остальные поля проверяются аннотациями.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FilmValidationException extends RuntimeException {
    public FilmValidationException(String message) {
        super(message);
    }
}
