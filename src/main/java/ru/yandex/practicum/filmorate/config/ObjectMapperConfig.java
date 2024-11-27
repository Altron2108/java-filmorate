package ru.yandex.practicum.filmorate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectMapperConfig {
    public static ObjectMapper configureObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Регистрация модуля для поддержки LocalDate и других типов Java 8 Date/Time API
        mapper.registerModule(new JavaTimeModule());
        // Отключение записи дат в виде timestamp
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
