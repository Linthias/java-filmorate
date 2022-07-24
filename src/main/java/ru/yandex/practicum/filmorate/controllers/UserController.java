package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

/*
Обработчик запросов к пользовательскому сервису.
 */

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User post(@Valid @RequestBody User user) {

        user = service.getUserStorage().add(user);

        log.info("POST /users: добавлен новый объект");
        return user;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {

        if (id < 1 || id > service.getUserStorage().getAll().size())
            throw new ObjectNotFoundException("bad user id: " + id);

        log.info("GET /users/" + id + ": получен объект");
        return service.getUserStorage().getAll().get(id - 1);
    }

    @GetMapping
    public List<User> getAll() {

        log.info("GET /users: возвращен список пользователей");
        return service.getUserStorage().getAll();
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {

        if (id < 1 || id > service.getUserStorage().getAll().size())
            throw new ObjectNotFoundException("bad user id: " + id);

        log.info("GET /users/" + id + "/friends: возвращен список друзей");
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {

        if (id < 1 || id > service.getUserStorage().getAll().size())
            throw new ObjectNotFoundException("bad user id: " + id);
        if (otherId < 1 || otherId > service.getUserStorage().getAll().size())
            throw new ObjectNotFoundException("bad user id: " + otherId);

        log.info("GET /users/" + id + "/friends/common/" + otherId + ": возвращен список общих друзей");
        return service.getCommonFriends(id, otherId);
    }

    @PutMapping
    public User change(@Valid @RequestBody User newUser) {

        if (newUser.getId() < 1 || newUser.getId() > service.getUserStorage().getAll().size())
            throw new ObjectNotFoundException("bad user id: " + newUser.getId());

        if (!service.getUserStorage().change(newUser)) {
            newUser = service.getUserStorage().add(newUser);
            log.info("PUT /users: добавлен новый объект");
        }

        log.info("PUT /users: объект изменен/добавлен");
        return newUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {

        if (id < 1 || id > service.getUserStorage().getAll().size())
            throw new ObjectNotFoundException("bad user id: " + id);
        if (friendId < 1 || friendId > service.getUserStorage().getAll().size())
            throw new ObjectNotFoundException("bad user id: " + friendId);

        service.addFriend(id, friendId);
        log.info("PUT /users/" + id + "/friends/" + friendId + ": друг добавлен");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {

        if (id < 1 || id > service.getUserStorage().getAll().size())
            throw new ObjectNotFoundException("bad user id: " + id);
        if (friendId < 1 || friendId > service.getUserStorage().getAll().size())
            throw new ObjectNotFoundException("bad user id: " + friendId);

        service.deleteFriend(id, friendId);
        log.info("DELETE /users/" + id + "/friends/" + friendId + ": друг удален");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public void UserValidationFailure(MethodArgumentNotValidException exception) {

    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void UserNotFound(ObjectNotFoundException exception) {

    }
}
