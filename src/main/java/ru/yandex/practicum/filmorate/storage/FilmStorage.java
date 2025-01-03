package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);

    Optional<Film> getFilmById(Long id);

    Collection<Film> findAll();

    Film update(Film film);

    boolean deleteFilm(int id);
}
