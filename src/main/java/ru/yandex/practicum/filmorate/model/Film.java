package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;

    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public Film() {
        this.name = "default name";
        this.description = "default description";
        this.releaseDate = LocalDate.of(2022, 5, 15);
        this.duration = 60;
        this.id = 1;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.id = 1;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.id = id;
    }
}
