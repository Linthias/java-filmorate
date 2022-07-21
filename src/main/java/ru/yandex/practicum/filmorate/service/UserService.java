package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
Класс-сервис для работы с пользователями.
Реализовано:
- добавление друга
- удаление друга
- вывод списка друзей
- вывод списка общих друзей
 */

@Getter
@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        userStorage.getAll().get(userId - 1).getFriends().add(friendId);
        userStorage.getAll().get(friendId - 1).getFriends().add(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        if (userStorage.getAll().get(userId - 1).getFriends().contains(friendId)) {
            userStorage.getAll().get(userId - 1).getFriends().remove(friendId);
            userStorage.getAll().get(friendId - 1).getFriends().remove(userId);
        } else
            throw new ObjectNotFoundException("bad user id: " + friendId);
    }

    public List<User> getFriends(int id) {
        List<User> friends = new ArrayList<>();

        for (Integer friendId : userStorage.getAll().get(id - 1).getFriends()) {
            friends.add(userStorage.getAll().get(friendId - 1));
        }
        return friends;
    }
    
    public List<User> getCommonFriends(int id, int otherId) {
        Set<Integer> commonIds = new HashSet<>(userStorage.getAll().get(id - 1).getFriends());
        List<User> commonFriends = new ArrayList<>();

        commonIds.retainAll(userStorage.getAll().get(otherId - 1).getFriends());
        for (Integer friendId : commonIds) {
            commonFriends.add(userStorage.getAll().get(friendId - 1));
        }

        return commonFriends;
    }
}
