package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import jakarta.validation.Valid;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/films")
@Validated
@Slf4j
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private int currentId = 1;

    // Создание фильма
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(currentId++);  // Присваиваем уникальный ID для каждого нового фильма
        films.add(film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    // Обновление фильма
    @PutMapping("/{id}")
    public Film updateFilm(@PathVariable int id, @Valid @RequestBody Film film) {
        Film existingFilm = films.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Фильм с ID " + id + " не найден"));

        existingFilm.setName(film.getName());
        existingFilm.setDescription(film.getDescription());
        existingFilm.setReleaseDate(film.getReleaseDate());
        existingFilm.setDuration(film.getDuration());

        log.info("Фильм обновлен: {}", existingFilm);
        return existingFilm;
    }

    // Удаление фильма
    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable int id) {
        Film existingFilm = films.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Фильм с ID " + id + " не найден"));

        films.remove(existingFilm);
        log.info("Фильм с ID {} удален", id);
    }

    // Получение всех фильмов
    @GetMapping
    public List<Film> getFilms() {
        log.info("Запрос на получение всех фильмов");
        return films;
    }

    // Сброс состояния (для тестов)
    public void resetData() {
        films.clear();
        currentId = 1;
    }
}
