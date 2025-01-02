package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film); // Создание нового фильма

    Optional<Film> getFilmById(Long id); // Получение фильма по ID

    Collection<Film> findAll(); // Получение всех фильмов

    Film update(Film film); // Обновление фильма

    boolean deleteFilm(int id); // Удаление фильма
}
