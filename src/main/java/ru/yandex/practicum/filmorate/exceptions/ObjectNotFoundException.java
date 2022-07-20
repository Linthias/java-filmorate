package ru.yandex.practicum.filmorate.exceptions;

/*
Исключение, возникающее при отсутствии нужного объекта.
 */

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
