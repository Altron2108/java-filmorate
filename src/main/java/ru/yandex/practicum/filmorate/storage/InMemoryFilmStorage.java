package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();  // Изменили тип на Long
    private long nextId = 1L;  // Тип nextId изменен на Long

    @Override
    public Film create(Film film) {
        film.setId(nextId++);  // Устанавливаем id как Long
        films.put(film.getId(), film);  // Используем Long в качестве ключа
        return film;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return Optional.ofNullable(films.get((long) id));  // Преобразуем id в Long
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с ID " + film.getId() + " не найден.");
        }
        films.put(film.getId(), film);  // Используем Long в качестве ключа
        return film;
    }

    @Override
    public boolean deleteFilm(int id) {
        return films.remove((long) id) != null;  // Преобразуем id в Long
    }
}
