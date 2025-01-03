package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public Film createFilm(Film film) {
        log.info("Creating a new film: {}", film);
        Film createdFilm = filmStorage.create(film);
        log.info("Film created successfully with ID: {}", createdFilm.getId());
        return createdFilm;
    }

    public Optional<Film> getFilmById(long id) {
        log.info("Fetching film with ID: {}", id);
        Optional<Film> film = filmStorage.getFilmById(id);
        if (film.isPresent()) {
            log.info("Film found: {}", film.get());
        } else {
            log.warn("Film with ID {} not found", id);
            throw new NotFoundException("Film with ID {} not found" + id);
        }
        return film;
    }

    public Film updateFilm(Film film) {
        log.info("Updating film with ID: {}", film.getId());
        Film updatedFilm = filmStorage.update(film);
        log.info("Film updated successfully: {}", updatedFilm);
        return updatedFilm;
    }

    public void deleteFilm(long id) {
        log.info("Deleting film with ID: {}", id);
        boolean isDeleted = filmStorage.deleteFilm((int) id);
        if (isDeleted) {
            log.info("Film with ID {} deleted successfully", id);
        } else {
            log.warn("Failed to delete film with ID {}. Film not found.", id);
        }
    }

    public List<Film> getAllFilms() {
        log.info("Fetching all films");
        List<Film> films = List.copyOf(filmStorage.findAll());
        log.info("{} films found", films.size());
        return films;
    }

    public void addLike(Long filmId, Long userId) {
        log.info("Adding like from user ID {} to film ID {}", userId, filmId);
        if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким Id не найден");
        }
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found"));
        film.getLikes().add(userId);
        filmStorage.update(film);
        log.info("Like added successfully to film ID {}", filmId);
    }

    public void removeLike(long filmId, long userId) {
        log.info("Removing like from user ID {} to film ID {}", userId, filmId);
        if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким Id не найден");
        }
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found"));
        film.getLikes().remove(userId);
        filmStorage.update(film);
        log.info("Like removed successfully from film ID {}", filmId);
    }

    public List<Film> getPopular(int count) {
        log.info("Fetching top {} popular films", count);
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
