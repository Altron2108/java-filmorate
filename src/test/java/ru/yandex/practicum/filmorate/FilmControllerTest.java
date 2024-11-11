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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        film.setName("");
        film.setDescription("Description of the test film");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(120);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }
}