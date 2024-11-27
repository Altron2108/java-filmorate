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
        // Инициализация моков
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
    }

    @Test
    void createFilm_ShouldReturnCreatedFilm() throws Exception {
        // Создаем объект Film
        Film film = new Film(1L, "Film Title", "Description",
                LocalDate.of(2020, 1, 1), 120);

        // Мокируем поведение FilmStorage
        when(filmStorage.create(any(Film.class))).thenReturn(film);

        // Настроим ObjectMapper для сериализации
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Подключает поддержку LocalDate

        // Сериализуем объект Film в JSON строку
        String filmJson = objectMapper.writeValueAsString(film);
        System.out.println("Serialized Film JSON: " + filmJson);  // Для отладки

        // Выполняем POST-запрос
        MvcResult result = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(filmJson))
                .andDo(print())  // Печатает ответ для отладки
                .andExpect(status().isCreated())  // Проверяем, что статус Created
                .andReturn();

        // Получаем содержимое ответа
        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);  // Для отладки

        // Проверяем, что ответ содержит ожидаемые значения
        assertTrue(responseContent.contains("Film Title"), "Response does not contain 'Film Title'");
        assertTrue(responseContent.contains("Description"), "Response does not contain 'Description'");
        assertTrue(responseContent.contains("2020-01-01"), "Response does not contain '2020-01-01'");
        assertTrue(responseContent.contains("120"), "Response does not contain '120'");
    }


    @Test
    void getAllFilms_ShouldReturnFilms() throws Exception {
        // Создаем объект Film
        Film film = new Film(1L, "Film Title", "Description",
                LocalDate.of(2020, 1, 1), 120);

        // Мокируем поведение FilmStorage
        when(filmStorage.findAll()).thenReturn(Collections.singletonList(film));

        // Выполняем GET-запрос для получения всех фильмов
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())  // Проверяем статус OK
                .andExpect(jsonPath("$[0].name").value("Film Title"))
                // Проверяем, что имя фильма соответствует
                .andExpect(jsonPath("$[0].description").value("Description"))
                // Проверяем описание
                .andExpect(jsonPath("$[0].releaseDate").value("2020-01-01"))
                // Проверяем дату выпуска
                .andExpect(jsonPath("$[0].duration").value(120));  // Проверяем длительность
    }
}
