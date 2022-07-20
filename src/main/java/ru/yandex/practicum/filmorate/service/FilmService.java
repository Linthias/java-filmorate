package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmPopularityComparator;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
Класс-сервис для работы с фильмами в хранилище.
Реализовано:
- добавление лайка фильму
- удаление лайка
- вывод нужного количества фильмов по популярности
 */

@Getter
@Service
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int userId, int filmId) {
        if (filmId < 1 || filmId > filmStorage.getFilms().size())
            throw new ObjectNotFoundException("bad film id: " + filmId);
        if (userId < 1)
            throw new ObjectNotFoundException("bad user id: " + userId);

        filmStorage.getFilms().get(filmId-1).getLikes().add(userId);
    }

    public void deleteLike(int userId, int filmId) {
        if (filmId < 1 || filmId > filmStorage.getFilms().size())
            throw new ObjectNotFoundException("bad film id: " + filmId);
        if (filmStorage.getFilms().get(filmId-1).getLikes().contains(userId)) {
            filmStorage.getFilms().get(filmId-1).getLikes().remove(userId);
        } else
            throw new ObjectNotFoundException("bad user id: " + userId);
    }

    public List<Film> getTopFilms(int topSize) {
        if (topSize < 1 || topSize > filmStorage.getFilms().size())
            topSize = 10;

        List<Film> result = new ArrayList<>(filmStorage.getFilms());

        return result.stream()
                .sorted(new FilmPopularityComparator().reversed())
                .limit(topSize)
                .collect(Collectors.toList());
    }
}
