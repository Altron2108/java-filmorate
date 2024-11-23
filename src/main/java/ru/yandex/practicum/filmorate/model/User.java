package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;

    @Email(message = "Некорректный формат электронной почты")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;

    // Пустой конструктор
    public User() {
    }

    // Конструктор с параметрами
    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = (name == null || name.isBlank()) ? "Anonymous" : name;
        this.birthday = birthday;
    }

    // Метод сброса данных пользователя
    public void resetData() {
        this.email = "default@example.com"; // Значение по умолчанию для email
        this.login = "defaultLogin"; // Значение по умолчанию для логина
        this.name = "Anonymous"; // Значение по умолчанию для имени
        this.birthday = LocalDate.of(1970, 1, 1); // Дата по умолчанию (пример)
    }
}
