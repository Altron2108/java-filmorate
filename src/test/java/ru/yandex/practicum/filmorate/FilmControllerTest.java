package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        reset(filmService); // Сбрасываем состояние мока FilmService перед каждым тестом
    }

    @Test
    public void shouldAddFilmSuccessfully() throws Exception {
        Film film = new Film();
        film.setId(1); // Устанавливаем id
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(120);

        // Настраиваем поведение мока FilmService
        when(filmService.createFilm(film)).thenReturn(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Film")) // Проверка поля name
                .andExpect(jsonPath("$.id").value(1)); // Проверка поля id
    }

    @Test
    public void shouldUpdateFilmSuccessfully() throws Exception {
        Film film = new Film();
        film.setId(1); // Устанавливаем начальный id
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(120);

        // Настраиваем поведение мока для метода createFilm
        when(filmService.createFilm(any(Film.class))).thenAnswer(invocation -> {
            Film savedFilm = invocation.getArgument(0);
            savedFilm.setId(1); // Присваиваем ID для фильма
            return savedFilm;
        });

        // Выполняем запрос на создание фильма и получаем его id
        MvcResult result = mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Film createdFilm = objectMapper.readValue(responseContent, Film.class);
        int filmId = createdFilm.getId();

        // Обновляем название фильма
        film.setName("Updated Film");

        // Настраиваем мок для updateFilm
        when(filmService.updateFilm(filmId, film)).thenReturn(java.util.Optional.of(film));

        // Выполняем запрос на обновление фильма
        mockMvc.perform(put("/films/" + filmId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Film"));
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonexistentFilm() throws Exception {
        Film film = new Film();
        film.setName("Updated Film");
        film.setDescription("Updated Description");
        film.setReleaseDate(LocalDate.of(2023, 2, 2));
        film.setDuration(130);

        mockMvc.perform(put("/films/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Фильм с ID 99 не найден"));
    }

    @Test
    public void shouldDeleteFilmSuccessfully() throws Exception {
        Film film = new Film();
        film.setName("Film To Delete");
        film.setDescription("Description of Film to Delete");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(110);

        // Настройка мока для метода createFilm, чтобы он возвращал фильм с ID 1
        when(filmService.createFilm(any(Film.class))).thenAnswer(invocation -> {
            Film savedFilm = invocation.getArgument(0);
            savedFilm.setId(1); // Присваиваем ID для фильма
            return savedFilm;
        });

        // Создаём фильм и проверяем ответ
        MvcResult result = mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Film createdFilm = objectMapper.readValue(responseContent, Film.class);
        int filmId = createdFilm.getId();

        // Настройка мока для метода deleteFilm, чтобы он успешно выполнял удаление
        when(filmService.deleteFilm(filmId)).thenReturn(true);

        // Выполняем запрос на удаление фильма
        mockMvc.perform(delete("/films/" + filmId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonexistentFilm() throws Exception {
        mockMvc.perform(delete("/films/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Фильм с ID 99 не найден"));
    }
}
