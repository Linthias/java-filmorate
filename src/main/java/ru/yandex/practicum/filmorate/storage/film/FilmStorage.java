package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/*
Интерфейс хранилища фильмов.
 */

public interface FilmStorage {

    void addFilm(Film film);
    List<Film> getFilms();
    boolean changeFilm(Film newFilm);
}
