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
        // Создаем пользователя, которого будем обновлять
        String userJson = "{\"login\":\"doloreUpdate\", \"name\":\"est adipisicing\", \"id\":0, \"email\":\"mail@yandex.ru\", \"birthday\":\"1976-09-20\"}";

        // Сначала добавляем пользователя
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        // Обновляем пользователя
        String updatedUserJson = "{\"login\":\"doloreUpdate\", \"name\":\"Updated Name\", \"id\":0, \"email\":\"updated_email@yandex.ru\", \"birthday\":\"1976-09-20\"}";

        // Выполняем обновление пользователя
        mockMvc.perform(put("/users/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated_email@yandex.ru"));
    }

}
