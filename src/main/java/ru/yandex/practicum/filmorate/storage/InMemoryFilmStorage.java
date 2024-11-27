package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @Override
    public Film create(Film film) {
        film.setId((long) nextId++);
        films.put(Math.toIntExact(film.getId()), film);
        return film;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new IllegalArgumentException("Фильм с ID " + film.getId() + " не найден.");
        }
        films.put(Math.toIntExact(film.getId()), film);
        return film;
    }

    @Override
    public boolean deleteFilm(int id) {
        return films.remove(id) != null;
    }
}
