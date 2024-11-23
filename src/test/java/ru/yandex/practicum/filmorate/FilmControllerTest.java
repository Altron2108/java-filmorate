package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;

    private Film testFilm;

    @BeforeEach
    public void setUp() {
        reset(filmService); // Сбрасываем состояние мока
        testFilm = new Film();
        testFilm.setId(1);
        testFilm.setName("Test Film");
        testFilm.setDescription("Test Description");
        testFilm.setReleaseDate(LocalDate.of(2023, 1, 1));
        testFilm.setDuration(120);
    }

    @Test
    public void shouldAddFilmSuccessfully() throws Exception {
        when(filmService.createFilm(any(Film.class))).thenReturn(testFilm);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFilm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Film"));

        verify(filmService, times(1)).createFilm(any(Film.class));
    }

    @Test
    public void shouldUpdateFilmSuccessfully() throws Exception {
        testFilm.setName("Updated Film");
        when(filmService.updateFilm(eq(1), any(Film.class))).thenReturn(Optional.of(testFilm));

        mockMvc.perform(put("/films/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFilm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Film"));

        verify(filmService, times(1)).updateFilm(eq(1), any(Film.class));
    }

    @Test
    public void shouldDeleteFilmSuccessfully() throws Exception {
        when(filmService.deleteFilm(1)).thenReturn(true);

        mockMvc.perform(delete("/films/1"))
                .andExpect(status().isNoContent());

        verify(filmService, times(1)).deleteFilm(1);
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentFilm() throws Exception {
        when(filmService.deleteFilm(999)).thenReturn(false);

        mockMvc.perform(delete("/films/999"))
                .andExpect(status().isNotFound());

        verify(filmService, times(1)).deleteFilm(999);
    }

    @Test
    public void shouldReturnBadRequestForInvalidFilmData() throws Exception {
        testFilm.setName(""); // Некорректное имя фильма
        when(filmService.createFilm(any(Film.class))).thenReturn(testFilm);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFilm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetFilmByIdSuccessfully() throws Exception {
        when(filmService.getFilmById(1)).thenReturn(Optional.of(testFilm));

        mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Film"));

        verify(filmService, times(1)).getFilmById(1);
    }

    @Test
    public void shouldReturnNotFoundWhenGettingNonExistentFilm() throws Exception {
        when(filmService.getFilmById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/films/999"))
                .andExpect(status().isNotFound());

        verify(filmService, times(1)).getFilmById(999);
    }
}
