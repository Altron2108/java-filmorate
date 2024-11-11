package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updateUser_shouldReturnStatusOk() throws Exception {
        // Создаем JSON строку для начального пользователя
        String userJson = "{\"login\":\"doloreUpdate\", \"name\":\"est adipisicing\", " +
                "\"id\":0, \"email\":\"mail@yandex.ru\", \"birthday\":\"1976-09-20\"}";

        // Добавляем пользователя через POST запрос
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        // Подготавливаем JSON для обновленного пользователя
        String updatedUserJson = "{\"login\":\"doloreUpdate\", \"name\":\"Updated Name\", \"id\":0, " +
                "\"email\":\"updated_email@yandex.ru\", \"birthday\":\"1976-09-20\"}";

        // Отправляем PUT запрос для обновления пользователя
        mockMvc.perform(put("/users/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk()) // Проверяем, что статус ответа - OK
                .andExpect(jsonPath("$.name").value("Updated Name"))
                // Проверка обновленного имени
                .andExpect(jsonPath("$.email").value("updated_email@yandex.ru"));
        // Проверка обновленного email
    }
}
