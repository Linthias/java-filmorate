package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @EqualsAndHashCode.Exclude
    private int id;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;

    private String name;

    @NotNull
    @Past
    private LocalDate birthday;

    private Set<Integer> friends;

    public User() {
        this.email = "default@yandex.ru";
        this.login = "default";
        this.name = "default name";
        this.birthday = LocalDate.of(2022, 5, 15);
        this.id = 1;
        this.friends = new HashSet<>();
    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.id = 1;
        this.friends = new HashSet<>();
    }

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.id = id;
        this.friends = new HashSet<>();
    }
}
