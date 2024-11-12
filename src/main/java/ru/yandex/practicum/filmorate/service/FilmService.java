package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FilmService {

    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final List<Film> films = new ArrayList<>();

    public Film createFilm(Film film) {
        film.setId(idCounter.getAndIncrement());
        films.add(film);
        return film;
    }

    public Optional<Film> getFilmById(int id) {
        return films.stream()
                .filter(film -> film.getId() == id)
                .findFirst();
    }

    public Optional<Film> updateFilm(int id, Film film) {
        return getFilmById(id).map(existingFilm -> {
            existingFilm.setName(film.getName());
            existingFilm.setDescription(film.getDescription());
            existingFilm.setReleaseDate(film.getReleaseDate());
            existingFilm.setDuration(film.getDuration());
            return existingFilm;
        });
    }

    public boolean deleteFilm(int id) {
        return films.removeIf(film -> film.getId() == id);
    }

    public List<Film> getAllFilms() {
        return films;
    }
}
