package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        Film createdFilm = filmService.createFilm(film);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable int id) {
        Optional<Film> film = filmService.getFilmById(id);
        return film.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Фильм с ID " + id + " не найден"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable int id, @Valid @RequestBody Film film) {
        Optional<Film> updatedFilm = filmService.updateFilm(id, film);
        return updatedFilm.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Фильм с ID " + id + " не найден"));
    }

    @PutMapping("/{id}/reset")
    public ResponseEntity<Film> resetFilmData(@PathVariable int id) {
        Optional<Film> film = filmService.getFilmById(id);
        return film.map(f -> {
                    f.resetData();
                    filmService.updateFilm(id, f); // Обновление фильма после сброса данных
                    return new ResponseEntity<>(f, HttpStatus.OK);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Фильм с ID " + id + " не найден"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable int id) {
        if (filmService.deleteFilm(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + id + " не найден");
        }
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        return new ResponseEntity<>(films, HttpStatus.OK);
    }
}
