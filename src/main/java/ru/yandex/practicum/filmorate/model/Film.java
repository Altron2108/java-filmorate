package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
public class Film {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может превышать 200 символов")
    private String description;

    @PastOrPresent(message = "Дата выпуска должна быть в прошлом или настоящем")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive(message = "Длительность должна быть положительным числом")
    private int duration;

    @Getter
    private final Set<Long> likes = new HashSet<>();

    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addLike(long userId) {
        boolean isAdded = likes.add(userId);
        if (!isAdded) {
            log.warn("User ID {} already liked this film.", userId);
        }
    }

    public boolean removeLike(long userId) {
        boolean isRemoved = likes.remove(userId);
        if (!isRemoved) {
            log.warn("User ID {} has not liked this film.", userId);
        }
        return isRemoved;
    }

}
