package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@WebMvcTest(UserController.class)  // Тестируем только UserController
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Тест на создание пользователя
    @Test
    public void createUser_shouldReturnStatusCreated() throws Exception {
        User user = new User();
        user.setLogin("johndoe");
        user.setEmail("john.doe@example.com");
        user.setName("John Doe");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())  // Ожидаем статус 201
                .andExpect(jsonPath("$.id").value(1))  // Проверка id
                .andExpect(jsonPath("$.name").value("John Doe"));  // Проверка имени
    }

    // Тест на обновление пользователя
    @Test
    public void updateUser_shouldReturnStatusOk() throws Exception {
        User user = new User();
        user.setLogin("johndoe");
        user.setEmail("john.doe@example.com");
        user.setName("John Doe");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // Создаем пользователя
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        // Обновляем пользователя
        user.setName("Updated John Doe");

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk()) // Ожидаем статус 200
                .andExpect(jsonPath("$.name").value("Updated John Doe"));
    }

    // Тест на удаление пользователя
    @Test
    public void deleteUser_shouldReturnStatusNoContent() throws Exception {
        User user = new User();
        user.setLogin("johndoe");
        user.setEmail("john.doe@example.com");
        user.setName("John Doe");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // Создаем пользователя
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        // Удаляем пользователя
        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Ожидаем статус 204
    }
}
