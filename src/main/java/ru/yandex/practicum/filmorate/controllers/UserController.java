package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
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
    public User postUser(@Valid @RequestBody User user) {
        User tempUser = service.getUserStorage().addUser(user);

        log.info("POST /users: добавлен новый объект");
        return tempUser;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        if (id < 1 || id > service.getUserStorage().getUsers().size())
            throw new ObjectNotFoundException("bad user id: " + id);

        User response = null;

        for (User user : service.getUserStorage().getUsers()) {
            if (user.getId() == id) {
                response = user;
                break;
            }
        }

        log.info("GET /users/" + id + ": получен объект");
        return response;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("GET /users: возвращен список пользователей");
        return service.getUserStorage().getUsers();
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        if (id < 1 || id > service.getUserStorage().getUsers().size())
            throw new ObjectNotFoundException("bad user id: " + id);

        List<User> response = new ArrayList<>();

        for (Integer friendId : service.getUserStorage().getUsers().get(id - 1).getFriends()) {
            response.add(service.getUserStorage().getUsers().get(friendId - 1));
        }

        log.info("GET /users/" + id + "/friends: возвращен список друзей");
        return response;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getUserCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        if (id < 1 || id > service.getUserStorage().getUsers().size())
            throw new ObjectNotFoundException("bad user id: " + id);
        if (otherId < 1 || otherId > service.getUserStorage().getUsers().size())
            throw new ObjectNotFoundException("bad user id: " + otherId);

        log.info("GET /users/" + id + "/friends/common/" + otherId + ": возвращен список общих друзей");
        return service.getCommonFriends(id, otherId);
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() < 1 || newUser.getId() > service.getUserStorage().getUsers().size())
            throw new ObjectNotFoundException("bad user id: " + newUser.getId());

        if (!service.getUserStorage().changeUser(newUser)) {
            newUser = service.getUserStorage().addUser(newUser);
            log.info("PUT /users: добавлен новый объект");
        }

        log.info("PUT /users: объект изменен/добавлен");
        return newUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        if (id < 1 || id > service.getUserStorage().getUsers().size())
            throw new ObjectNotFoundException("bad user id: " + id);
        if (friendId < 1 || friendId > service.getUserStorage().getUsers().size())
            throw new ObjectNotFoundException("bad user id: " + friendId);

        service.addFriend(id, friendId);
        log.info("PUT /users/" + id + "/friends/" + friendId + ": друг добавлен");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        if (id < 1 || id > service.getUserStorage().getUsers().size())
            throw new ObjectNotFoundException("bad user id: " + id);
        if (friendId < 1 || friendId > service.getUserStorage().getUsers().size())
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
