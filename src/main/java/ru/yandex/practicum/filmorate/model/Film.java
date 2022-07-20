package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Size(min = 1, max = 200)
    private String description;

    // не смог определить,
    // как сделать подходящую проверку с помощью аннотаций,
    // поэтому это единственное поле,
    // для которого используется FilmValidationException
    @NotNull
    private LocalDate releaseDate;

    @Min(1)
    private int duration;

    private Set<Integer> likes;

    public Film() {
        this.name = "default name";
        this.description = "default description";
        this.releaseDate = LocalDate.of(2022, 5, 15);
        this.duration = 60;
        this.id = 1;
        this.likes = new HashSet<>();
    }

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.id = 1;
        this.likes = new HashSet<>();
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.id = id;
        this.likes = new HashSet<>();
    }
}
