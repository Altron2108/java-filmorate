package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getFilmById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Фильм с ID " + id + " не найден"));
    }

    @PutMapping("/{id}")
    public Film updateFilm(@PathVariable int id, @Valid @RequestBody Film film) {
        return filmService.updateFilm(id, film)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Фильм с ID " + id + " не найден"));
    }

    @PutMapping("/{id}/reset")
    public Film resetFilmData(@PathVariable int id) {
        return filmService.getFilmById(id)
                .map(film -> {
                    film.resetData();
                    return film;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Фильм с ID " + id + " не найден"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(@PathVariable int id) {
        if (!filmService.deleteFilm(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + id + " не найден");
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }
}
