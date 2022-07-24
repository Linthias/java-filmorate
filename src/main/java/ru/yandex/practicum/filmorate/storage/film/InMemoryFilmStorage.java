package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

/*
Хранилище фильмов. Реализовано:
- добавление нового фильма
- получение списка фильмов
- изменение существующего фильма
 */

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private List<Film> films;

    public InMemoryFilmStorage() {
        films = new ArrayList<>();
    }

    @Override
    public void add(Film film) {
        film.setId(films.size() + 1);
        films.add(film);
    }

    @Override
    public List<Film> getAll() {
        return films;
    }

    @Override
    public boolean change(Film newFilm) {
        boolean hasFilm = false;

        for (Film film : films) {
            if (film.getId() == newFilm.getId()) {
                films.remove(film);
                films.add(newFilm);
                hasFilm = true;
                break;
            }
        }
        return hasFilm;
    }
}
