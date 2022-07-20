package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

/*
Хранилище пользователей. Реализованы базовые функции:
- добавление нового пользователя
- получение списка пользователей
- изменение существующего пользователя
 */

@Component
public class InMemoryUserStorage implements UserStorage {
    private List<User> users;

    public InMemoryUserStorage() {
        users = new ArrayList<>();
    }

    @Override
    public User addUser(User user) {
        // если имя пустое, то им становится логин
        if (user.getName() == null || user.getName().equals(""))
            user.setName(user.getLogin());

        user.setId(users.size() + 1);

        users.add(user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public boolean changeUser(User newUser) {
        boolean hasUser = false;

        if (newUser.getName() == null || newUser.getName().equals(""))
            newUser.setName(newUser.getLogin());

        for (User user : users) {
            if (user.getId() == newUser.getId()) {
                users.remove(user);
                users.add(newUser);
                hasUser = true;
                break;
            }
        }

        return hasUser;
    }
}
