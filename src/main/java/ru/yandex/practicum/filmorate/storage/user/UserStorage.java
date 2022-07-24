package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

/*
Интерфейс хранилища пользователей.
 */

public interface UserStorage {

    User add(User user);
    List<User> getAll();
    boolean change(User newUser);
}
