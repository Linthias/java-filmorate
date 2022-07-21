package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/*
Интерфейс хранилища фильмов.
 */

public interface FilmStorage {

    void add(Film film);
    List<Film> getAll();
    boolean change(Film newFilm);
}
