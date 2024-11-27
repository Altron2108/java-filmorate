package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.config.ObjectMapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmSerializationExample {
    public static void main(String[] args) throws Exception {
        // Получение настроенного ObjectMapper
        ObjectMapper mapper = ObjectMapperConfig.configureObjectMapper();

        // Создание объекта Film
        Film film = new Film(1L, "Test Film", "A description of the film",
                LocalDate.of(2000, 1, 1), 120);

        // Сериализация объекта в JSON
        String json = mapper.writeValueAsString(film);
        System.out.println("Serialized JSON: " + json);

        // Десериализация JSON обратно в объект
        Film deserializedFilm = mapper.readValue(json, Film.class);
        System.out.println("Deserialized Film: " + deserializedFilm);
    }
}
