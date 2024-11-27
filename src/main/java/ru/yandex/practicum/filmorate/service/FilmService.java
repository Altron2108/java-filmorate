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
    private final CopyOnWriteArrayList<Film> films = new CopyOnWriteArrayList<>();

    public Film createFilm(Film film) {
        validateFilm(film);
        film.setId((long) idCounter.getAndIncrement());
        films.add(film);
        return film;
    }

    public Optional<Film> getFilmById(long id) {
        return films.stream().filter(film -> film.getId() == id).findFirst();
    }

    public Film updateFilm(long id, Film updatedFilm) {
        validateFilm(updatedFilm);
        Film existingFilm = getFilmById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с ID " + id + " не найден."));
        existingFilm.setName(updatedFilm.getName());
        existingFilm.setDescription(updatedFilm.getDescription());
        existingFilm.setReleaseDate(updatedFilm.getReleaseDate());
        existingFilm.setDuration(updatedFilm.getDuration());
        return existingFilm;
    }

    public boolean deleteFilm(long id) {
        return films.removeIf(film -> film.getId() == id);
    }

    public List<Film> getAllFilms() {
        return List.copyOf(films);
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
