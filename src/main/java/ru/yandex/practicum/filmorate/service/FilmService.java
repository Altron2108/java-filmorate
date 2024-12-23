package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        log.info("Creating a new film: {}", film);
        Film createdFilm = filmStorage.create(film);
        log.info("Film created successfully with ID: {}", createdFilm.getId());
        return createdFilm;
    }

    public Optional<Film> getFilmById(long id) {
        log.info("Fetching film with ID: {}", id);
        Optional<Film> film = filmStorage.getFilmById((int) id);
        if (film.isPresent()) {
            log.info("Film found: {}", film.get());
        } else {
            log.warn("Film with ID {} not found", id);
        }
        return film;
    }

    public Film updateFilm(Film film) {
        log.info("Updating film with ID: {}", film.getId());
        Film updatedFilm = filmStorage.update(film);
        log.info("Film updated successfully: {}", updatedFilm);
        return updatedFilm;
    }

    public boolean deleteFilm(long id) {
        log.info("Deleting film with ID: {}", id);
        boolean isDeleted = filmStorage.deleteFilm((int) id);
        if (isDeleted) {
            log.info("Film with ID {} deleted successfully", id);
        } else {
            log.warn("Failed to delete film with ID {}. Film not found.", id);
        }
        return isDeleted;
    }

    public List<Film> getAllFilms() {
        log.info("Fetching all films");
        List<Film> films = List.copyOf(filmStorage.findAll()); // Используем findAll
        log.info("{} films found", films.size());
        return films;
    }

}
