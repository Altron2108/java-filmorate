package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmControllerTest {

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController filmController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void createFilm_ShouldReturnCreatedFilm() throws Exception {
        Film film = new Film("Film Title", "Description",
                LocalDate.of(2020, 1, 1), 120);

        Film createdFilm = new Film("Film Title", "Description",
                LocalDate.of(2020, 1, 1), 120);
        createdFilm.setId(1L);

        when(filmService.createFilm(film)).thenReturn(createdFilm);

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(filmJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Film Title"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.releaseDate").value("2020-01-01"))
                .andExpect(jsonPath("$.duration").value(120));
    }

    @Test
    void getAllFilms_ShouldReturnFilms() throws Exception {
        Film film = new Film("Film Title", "Description",
                LocalDate.of(2020, 1, 1), 120);
        film.setId(1L);

        when(filmService.getAllFilms()).thenReturn(Collections.singletonList(film));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Film Title"))
                .andExpect(jsonPath("$[0].description").value("Description"))
                .andExpect(jsonPath("$[0].releaseDate").value("2020-01-01"))
                .andExpect(jsonPath("$[0].duration").value(120));
    }

    @Test
    void getFilmById_ShouldReturnNotFound() throws Exception {
        when(filmService.getFilmById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/films/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createFilm_InvalidData_ShouldReturnBadRequest() throws Exception {
        Film invalidFilm = new Film("", "", LocalDate.of(2020, 1, 1), 0);
        String invalidFilmJson = objectMapper.writeValueAsString(invalidFilm);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(invalidFilmJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addLike_ShouldAddLikeToFilm() {
        Film film = new Film("Film Title", "Description",
                LocalDate.of(2020, 1, 1), 120);
        film.setId(1L);

        film.addLike(100L);

        // Проверка, что лайк был добавлен
        assertTrue(film.getLikes().contains(100L));
    }
}