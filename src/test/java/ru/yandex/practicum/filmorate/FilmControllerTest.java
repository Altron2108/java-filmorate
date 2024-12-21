package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmControllerTest {

    @Mock
    private FilmStorage filmStorage;

    @InjectMocks
    private FilmController filmController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
    }

    @Test
    void createFilm_ShouldReturnCreatedFilm() throws Exception {
        // Создаем объект Film без id
        Film film = new Film("Film Title", "Description",
                LocalDate.of(2020, 1, 1), 120);

        // Мокируем поведение FilmStorage: возвращаем объект с генерированным id
        Film createdFilm = new Film("Film Title", "Description",
                LocalDate.of(2020, 1, 1), 120);
        createdFilm.setId(1L); // Устанавливаем id для теста

        when(filmStorage.create(any(Film.class))).thenReturn(createdFilm);

        // Настроим ObjectMapper для сериализации
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Подключает поддержку LocalDate

        // Сериализуем объект Film в JSON строку
        String filmJson = objectMapper.writeValueAsString(film);

        // Выполняем POST-запрос
        MvcResult result = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(filmJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        // Проверяем содержимое ответа
        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("Film Title"));
        assertTrue(responseContent.contains("Description"));
        assertTrue(responseContent.contains("2020-01-01"));
        assertTrue(responseContent.contains("120"));
    }

    @Test
    void getAllFilms_ShouldReturnFilms() throws Exception {
        // Создаем объект Film
        Film film = new Film("Film Title", "Description",
                LocalDate.of(2020, 1, 1), 120);
        film.setId(1L); // Устанавливаем id для теста

        // Мокируем поведение FilmStorage
        when(filmStorage.findAll()).thenReturn(Collections.singletonList(film));

        // Выполняем GET-запрос для получения всех фильмов
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Film Title"))
                .andExpect(jsonPath("$[0].description").value("Description"))
                .andExpect(jsonPath("$[0].releaseDate").value("2020-01-01"))
                .andExpect(jsonPath("$[0].duration").value(120));
    }
}
