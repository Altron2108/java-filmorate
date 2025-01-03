package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor // Генерирует конструктор по умолчанию
@AllArgsConstructor // Генерирует конструктор со всеми полями
public class User {
    private Long id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат Email")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения должна быть в прошлом")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    // Метод для установки друзей
    // Метод для получения друзей (уже генерируется через @Data, оставлено для ясности)
    private Set<Long> friends = new HashSet<>();

    // Конструктор с параметрами
    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name != null && !name.isBlank() ? name : login;
        this.birthday = birthday;
    }

    // Метод для добавления друга
    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    // Метод для удаления друга
    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }

}
