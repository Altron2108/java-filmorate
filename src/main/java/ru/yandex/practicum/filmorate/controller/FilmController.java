package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        log.info("Получен запрос на создание фильма с данными: {}", film);

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата выхода фильма должна быть позже 28.12.1895");
        }

        log.info("Создание нового фильма: {}", film.getName());
        Film createdFilm = filmService.createFilm(film);
        log.info("Фильм создан: {}", createdFilm);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);  // Возвращаем созданный объект
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма с id = {}", film.getId());
        return filmService.updateFilm(film);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> findAll() {
        log.info("Запрос на получение всех фильмов");
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id)
                .map(ResponseEntity::ok) // Если фильм найден, возвращаем его с 200 OK
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Если фильм не найден,
        // возвращаем 404
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable long id) {
        log.info("Запрос на удаление фильма с id = {}", id);
        filmService.deleteFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с id = {} поставил лайк фильму с id = {}", userId, id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с id = {} удалил лайк с фильма с id = {}", userId, id);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрос на получение популярных фильмов с количеством: {}", count);
        return filmService.getPopular(count);
    }
}
