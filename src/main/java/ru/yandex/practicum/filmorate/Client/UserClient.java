package ru.yandex.practicum.filmorate.Client;

import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserClient {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // Создаем объект User с обновленными данными
        User updatedUser = new User();
        updatedUser.setId(1);  // Укажите существующий ID пользователя
        updatedUser.setEmail("mail@yandex.ru");
        updatedUser.setLogin("doloreUpdate");
        updatedUser.setName("est adipisicing");
        updatedUser.setBirthday(LocalDate.of(1976, 9, 20));

        // Выполняем PUT запрос для обновления пользователя
        String url = "http://localhost:8080/users/1";  // Убедитесь, что ID совпадает
        try {
            // Отправляем PUT-запрос
            restTemplate.put(url, updatedUser);
            System.out.println("Пользователь успешно обновлен!");
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении пользователя: " + e.getMessage());
        }
    }
}
