package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private List<User> users = new ArrayList<>();

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        log.info("POST /users: начало обработки эндпоинта");
        User tempUser = checkUser(user);
        log.info("POST /users: успешная проверка ввода");
        users.add(tempUser);
        log.info("POST /users: добавлен новый объект");
        return tempUser;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("GET /users: начало обработки эндпоинта");
        return users;
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User newUser) {
        log.info("PUT /users: начало обработки эндпоинта");
        boolean hasUser = false;
        User tempUser = newUser;
        for (User user : users) {
            if (user.getId() == newUser.getId()) {
                log.info("PUT /users: найден объект для изменения");
                tempUser = checkUser(newUser);
                log.info("PUT /users: успешная проверка ввода");
                users.remove(user);
                users.add(tempUser);
                hasUser = true;
                log.info("PUT /users: объект изменен");
                break;
            }
        }
        if (!hasUser) {
            log.info("PUT /users: объект не найден");
            tempUser = checkUser(newUser);
            log.info("PUT /users: успешная проверка ввода");
            users.add(tempUser);
            log.info("PUT /users: добавлен новый объект");
        }
        return tempUser;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public void UserValidationFailure(MethodArgumentNotValidException exception) {

    }

    @Deprecated
    public User checkUser(User user) {
        log.info("начало проверки ввода");
        if (user.getId() < 0) {
            log.warn("неверный идентификатор");
            throw new UserValidationException("User has wrong id");
        }
        if (user.getEmail() == null || user.getEmail().equals("") || !user.getEmail().contains("@")) {
            log.warn("неверный адрес почты");
            throw new UserValidationException("User has wrong email");
        }
        if (user.getLogin() == null || user.getLogin().equals("") || user.getLogin().contains(" ")) {
            log.warn("неверный логин");
            throw new UserValidationException("User has wrong login");
        }
        if (user.getName() == null || user.getName().equals("")) {
            log.warn("пустое имя");
            user.setName(user.getLogin());
            log.info("имя соответствует логину");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("неверная дата рождения");
            throw new UserValidationException("User has wrong birthday");
        }
        return user;
    }
}
