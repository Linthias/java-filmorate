package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private List<Film> films = new ArrayList<>();

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        log.info("POST /films: начало обработки эндпоинта");
        checkFilm(film);
        log.info("POST /films: успешная проверка ввода");
        films.add(film);
        log.info("POST /films: добавлен новый объект");
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("GET /films: начало обработки эндпоинта");
        return films;
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film newFilm) {
        log.info("PUT /films: начало обработки эндпоинта");
        boolean hasFilm = false;
        for (Film film : films) {
            if (film.getId() == newFilm.getId()) {
                log.info("PUT /films: найден объект для изменения");
                checkFilm(newFilm);
                log.info("PUT /films: успешная проверка ввода");
                films.remove(film);
                films.add(newFilm);
                hasFilm = true;
                log.info("PUT /films: объект изменен");
                break;
            }
        }
        if (!hasFilm) {
            log.info("PUT /films: объект не найден");
            checkFilm(newFilm);
            log.info("PUT /films: успешная проверка ввода");
            films.add(newFilm);
            log.info("PUT /films: добавлен новый объект");
        }
        return newFilm;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public void FilmValidationFailure(MethodArgumentNotValidException exception) {

    }

    @Deprecated
    public void checkFilm(Film film) {
        log.info("начало проверки ввода");
        if (film.getId() < 0) {
            log.warn("неверный идентификатор");
            throw new FilmValidationException("Film has wrong id");
        }
        if (film.getName().equals("") || film.getName() == null) {
            log.warn("пустое имя");
            throw new FilmValidationException("Film has empty name");
        }
        if (film.getDescription().length() > 200) {
            log.warn("слишком длинное описание");
            throw new FilmValidationException("Film has too long description");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.warn("неверная дата релиза");
            throw new FilmValidationException("Film has wrong release date");
        }
        if (film.getDuration() < 1) {
            log.warn("отрицательная продолжительность");
            throw new FilmValidationException("Film has negative duration");
        }
    }
}
