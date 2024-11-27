package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FilmService {

    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final List<Film> films = new CopyOnWriteArrayList<>();

    public Film createFilm(Film film) {
        validateFilm(film);
        film.setId((long) idCounter.getAndIncrement());
        films.add(film);
        return film;
    }

    public Optional<Film> getFilmById(int id) {
        return films.stream().filter(film -> film.getId() == id).findFirst();
    }

    public Optional<Film> updateFilm(int id, Film film) {
        validateFilm(film);
        return getFilmById(id).map(existingFilm -> {
            if (film.getName() != null) existingFilm.setName(film.getName());
            if (film.getDescription() != null) existingFilm.setDescription(film.getDescription());
            if (film.getReleaseDate() != null) existingFilm.setReleaseDate(film.getReleaseDate());
            if (film.getDuration() > 0) existingFilm.setDuration(film.getDuration());
            return existingFilm;
        });
    }

    public boolean deleteFilm(int id) {
        return films.removeIf(film -> film.getId() == id);
    }

    public List<Film> getAllFilms() {
        return films.stream()
                .map(f -> new Film(f.getName(), f.getDescription(), f.getReleaseDate(), f.getDuration()))
                .toList();
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new IllegalArgumentException("Название не может быть пустым.");
        }
        if (film.getDuration() <= 0) {
            throw new IllegalArgumentException("Продолжительность фильма должна быть положительной.");
        }

    }
}