package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilmServiceTest {

    @Test
    void testFilmIdInitialization() {
        FilmService filmService = new FilmService();
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("A short description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Film createdFilm = filmService.createFilm(film);
        assertNotNull(createdFilm.getId(), "ID должен быть инициализирован");
    }
}
