package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmSerializationTest {

    @Test
    void testFilmSerialization() throws Exception {
        // Создаем ObjectMapper и регистрируем модуль для Java 8
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Регистрация модуля
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Настройка для сериализации в формате ISO-8601

        Film film = new Film(1L, "Test Film", "Description",
                LocalDate.of(2000, 1, 1), 120);

        // Сериализация объекта в JSON
        String json = mapper.writeValueAsString(film);
        System.out.println("Serialized JSON: " + json);

        // Десериализация JSON обратно в объект
        Film deserializedFilm = mapper.readValue(json, Film.class);

        // Проверка равенства оригинального и десериализованного объекта
        assertEquals(film, deserializedFilm);
    }

}
