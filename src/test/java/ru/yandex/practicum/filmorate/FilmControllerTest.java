package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        // Сброс данных перед каждым тестом
        filmController.resetData();
    }

    @Test
    public void shouldAddFilmSuccessfully() throws Exception {
        // Создание объекта Film для добавления
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(120);

        // Проверяем добавление фильма
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isCreated()) // Ожидаем статус 201
                .andExpect(jsonPath("$.id").value(1)) // Проверяем, что ID фильма равен 1
                .andExpect(jsonPath("$.name").value("Test Film")); // Проверяем имя фильма
    }

    @Test
    public void shouldUpdateFilmSuccessfully() throws Exception {
        // Создание объекта Film для добавления
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(120);

        // Сначала добавляем фильм
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isCreated())  // Проверяем, что фильм добавлен
                .andExpect(jsonPath("$.id").value(1)) // Проверяем, что присвоен ID = 1
                .andExpect(jsonPath("$.name").value("Test Film")); // Проверяем имя фильма

        // Теперь обновляем фильм
        film.setName("Updated Film");

        // Выполняем запрос PUT для обновления
        mockMvc.perform(put("/films/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk()) // Проверяем, что обновление прошло успешно
                .andExpect(jsonPath("$.name").value("Updated Film")); // Проверяем новое имя
    }
}
