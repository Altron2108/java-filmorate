package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;

    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {

        log.info("Получен запрос на создание фильма с данными: {}", film);

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата выхода фильма должна быть позже 28.12.1895");
        }

        log.info("Создание нового фильма: {}", film.getName());
        Film createdFilm = filmStorage.create(film);
        log.info("Фильм создан: {}", createdFilm);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);  // Возвращаем созданный объект
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма с id = {}", film.getId());
        return ResponseEntity.ok(filmStorage.update(film));
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> findAll() {
        log.info("Запрос на получение всех фильмов");
        return ResponseEntity.ok(filmStorage.findAll());
    }
}
