package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        // Используем новый конструктор User
        user = new User("john.doe@example.com", "john_doe", "John Doe",
                LocalDate.of(1990, 5, 15));
        user.setId(1); // Устанавливаем id вручную для тестов
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john.doe@example.com\", \"login\":\"john_doe\", " +
                                "\"name\":\"John Doe\", \"birthday\":\"1990-05-15\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getUser_shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ID 1 не найден"));
    }


    @Test
    void getUser_shouldReturnUserWhenUserExists() throws Exception {
        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        User updatedUser = new User("updated@example.com", "updated_doe", "Updated Name",
                LocalDate.of(1991, 6, 20));
        updatedUser.setId(1); // Устанавливаем id вручную для тестов
        when(userService.updateUser(eq(1), any(User.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"updated@example.com\", \"login\":\"updated_doe\", " +
                                "\"name\":\"Updated Name\", \"birthday\":\"1991-06-20\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void updateUser_shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.updateUser(eq(1), any(User.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"updated@example.com\", \"login\":\"updated_doe\", " +
                                "\"name\":\"Updated Name\", \"birthday\":\"1991-06-20\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ID 1 не найден"));
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        when(userService.deleteUser(1)).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.deleteUser(1)).thenReturn(false);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ID 1 не найден"));
    }
}

