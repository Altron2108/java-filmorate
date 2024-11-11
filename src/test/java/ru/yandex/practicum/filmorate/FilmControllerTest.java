package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldAddFilmSuccessfully() throws Exception {
        Film film = new Film();
        film.setId(1);
        film.setName("Test Film");
        film.setDescription("Description of the test film");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(120);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Film"));
    }

    @Test
    public void shouldFailValidationForEmptyFilmName() throws Exception {
        Film film = new Film();
        film.setId(1);
        film.setName(""); // Пустое имя
        film.setDescription("Description of the test film");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(120);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailValidationForInvalidReleaseDate() throws Exception {
        Film film = new Film();
        film.setId(2);
        film.setName("Old Film");
        film.setDescription("Description of an old film");
        // Дата выпуска раньше 28 декабря 1895 года
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(120);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailValidationForNegativeDuration() throws Exception {
        Film film = new Film();
        film.setId(3);
        film.setName("Film with Negative Duration");
        film.setDescription("Film description");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(-120); // Отрицательная длительность

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailValidationForEmptyDescription() throws Exception {
        Film film = new Film();
        film.setId(4);
        film.setName("Film with Empty Description");
        film.setDescription(""); // Пустое описание
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(120);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }
}
