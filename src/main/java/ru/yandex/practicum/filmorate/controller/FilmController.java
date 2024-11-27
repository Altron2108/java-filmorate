package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ApiResponse;
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
    public ResponseEntity<ApiResponse<Film>> update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма с id = {}", film.getId());

        if (film.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ID фильма не может быть пустым", null));
        }

        Film updatedFilm = filmStorage.update(film);
        if (updatedFilm != null) {
            return ResponseEntity.ok(new ApiResponse<>("Фильм успешно обновлен", updatedFilm));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Фильм с id = " + film.getId() + " не найден", null));
        }
    }


    @GetMapping
    public ResponseEntity<Collection<Film>> findAll() {
        log.info("Запрос на получение всех фильмов");
        return ResponseEntity.ok(filmStorage.findAll());
    }
}
