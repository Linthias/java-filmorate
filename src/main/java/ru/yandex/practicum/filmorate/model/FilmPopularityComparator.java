package ru.yandex.practicum.filmorate.model;

import java.util.Comparator;

/*
Компаратор для сравнения фильмов по популярности.
 */

public class FilmPopularityComparator implements Comparator<Film> {
    @Override
    public int compare(Film o1, Film o2) {
        int result = 0;
        if (o1.getLikes().size() == 0)
            result = -1;
        else if (o2.getLikes().size() == 0)
            result = 1;
        else
            result = o1.getLikes().size() - o2.getLikes().size();
        return result;
    }
}
