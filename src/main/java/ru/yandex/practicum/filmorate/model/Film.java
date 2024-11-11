package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Film {

    private int id;

    @NotBlank(message = "Имя фильма не должно быть пустым.")
    private String name;

    @NotBlank(message = "Описание фильма не должно быть пустым.")
    private String description;

    @NotNull(message = "Дата выпуска не может быть пустой.")
    @PastOrPresent(message = "Дата выпуска должна быть в прошлом или настоящем.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительной.")
    private int duration;

    // Геттеры и сеттеры

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }
}
