package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        // Создаем объект User
        User user = new User("user@example.com", "userlogin", "User",
                LocalDate.of(1990, 1, 1));
        user.setId(1L);

        // Мокируем поведение userStorage
        when(userStorage.addUser(user)).thenReturn(user);

        // Настроим ObjectMapper для сериализации
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Подключает поддержку LocalDate

        // Сериализуем объект User в JSON строку
        String userJson = objectMapper.writeValueAsString(user);
        System.out.println("Serialized User JSON: " + userJson);  // Печать для отладки

        // Выполняем POST-запрос
        MvcResult result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(userJson)) // Используем сериализованный JSON
                .andDo(print())  // Печать ответа для отладки
                .andExpect(status().isCreated())  // Проверка статуса Created
                .andReturn();

        // Получаем содержимое ответа
        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);  // Печать для отладки

        // Проверяем, что ответ содержит ожидаемые значения
        assertTrue(responseContent.contains("user@example.com"),
                "Response does not contain 'user@example.com'");
        assertTrue(responseContent.contains("userlogin"), "Response does not contain 'userlogin'");
        assertTrue(responseContent.contains("User"), "Response does not contain 'User'");
        assertTrue(responseContent.contains("1990-01-01"), "Response does not contain '1990-01-01'");
    }

    @Test
    void getUser_ShouldReturnUser() throws Exception {
        User user = new User("user@example.com", "userlogin", "User", LocalDate.of
                (1990, 1, 1));
        user.setId(1L);

        when(userStorage.getUserById(1L)).thenReturn(java.util.Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.login").value("userlogin"));
    }

    @Test
    void getUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(userStorage.getUserById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        User user = new User("user@example.com", "userlogin", "UpdatedUser", LocalDate.of
                (1990, 1, 1));
        user.setId(1L);

        when(userStorage.updateUser(user)).thenReturn(java.util.Optional.of(user));

        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content("{\"id\":1,\"email\":\"user@example.com\",\"login\":\"userlogin\"," +
                                "\"name\":\"UpdatedUser\",\"birthday\":\"1990-01-01\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedUser"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent_WhenDeleted() throws Exception {
        when(userStorage.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenNotDeleted() throws Exception {
        when(userStorage.deleteUser(1L)).thenReturn(false);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }
}
