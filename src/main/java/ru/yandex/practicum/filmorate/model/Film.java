package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotEmpty(message = "Название не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    @PastOrPresent(message = "Дата релиза не может быть в будущем.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом.")
    private int duration;

    // Конструктор по умолчанию
    public Film() {
    }

    // Конструктор с параметрами
    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    // Метод сброса данных фильма
    public void resetData() {
        this.name = "Untitled"; // Название по умолчанию
        this.description = "No description available"; // Описание по умолчанию
        this.releaseDate = LocalDate.of(1970, 1, 1); // Дата релиза по умолчанию
        this.duration = 1; // Продолжительность по умолчанию
    }
}
