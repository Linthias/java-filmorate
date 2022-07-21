package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/*
Контроллер запросов к сервису фильмов.
 */

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping
    public Film post(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)))
            throw new FilmValidationException("Film has wrong release date");

        service.getFilmStorage().add(film);

        log.info("POST /films: добавлен новый объект");
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("GET /films: возвращен список фильмов");
        return service.getFilmStorage().getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable int id) {
        if (id < 1 || id > service.getFilmStorage().getAll().size())
            throw new ObjectNotFoundException("bad film id: " + id);

        log.info("GET /films/" + id + ": возвращен фильм");
        return service.getFilmStorage().getAll().get(id - 1);
    }

    @GetMapping("/popular")
    public List<Film> getPopularTop(@RequestParam(required = false) Integer count) {
        // на случай, если пользователь не указал параметр
        if (count == null)
            count = 0;

        log.info("GET /films/popular: возвращен топ фильмов");
        return service.getPopularTop(count);
    }

    @PutMapping
    public Film change(@Valid @RequestBody Film newFilm) {
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895,12,28)))
            throw new FilmValidationException("Film has wrong release date");
        if (newFilm.getId() < 1 || newFilm.getId() > service.getFilmStorage().getAll().size())
            throw new ObjectNotFoundException("bad film id: " + newFilm.getId());

        if (!service.getFilmStorage().change(newFilm)) {
            service.getFilmStorage().add(newFilm);
            log.info("PUT /films: добавлен новый объект");
        }

        log.info("PUT /films: объект изменен/добавлен");
        return newFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        service.addLike(userId, id);

        log.info("PUT /films/" + id + "/like/" + userId
                + ": количество лайков: " + service.getFilmStorage().getAll().get(id - 1).getLikes().size());
        return service.getFilmStorage().getAll().get(id - 1);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id, @PathVariable int userId) {
        service.deleteLike(userId, id);

        log.info("DELETE /films/" + id + "/like/" + userId
                + ": количество лайков: " + service.getFilmStorage().getAll().get(id - 1).getLikes().size());
        return service.getFilmStorage().getAll().get(id - 1);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void FilmValidationFailure(MethodArgumentNotValidException exception) {

    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void FilmNotFound(ObjectNotFoundException exception) {

    }
}
