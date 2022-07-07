package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
public class User {
    @EqualsAndHashCode.Exclude
    private int id;

    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User() {
        this.email = "default@yandex.ru";
        this.login = "default";
        this.name = "default name";
        this.birthday = LocalDate.of(2022, 5, 15);
        this.id = 1;
    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.id = 1;
    }

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.id = id;
    }
}
