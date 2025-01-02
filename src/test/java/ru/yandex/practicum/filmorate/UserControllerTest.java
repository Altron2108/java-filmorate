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
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @Mock
    private UserService userService;

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
        User user = new User("user@example.com", "userlogin",
                "User", LocalDate.of(1990, 1, 1));
        user.setId(1L);

        // Мокируем поведение userStorage
        when(userService.addUser(user)).thenReturn(user);

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
                .andExpect(status().isOk())  // Проверка статуса Created
                .andReturn();

        // Получаем содержимое ответа
        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);  // Печать для отладки

        // Проверяем, что ответ содержит ожидаемые значения
        assertTrue(responseContent.contains("user@example.com"), "Response does not contain 'user@example.com'")
        ;
        assertTrue(responseContent.contains("userlogin"), "Response does not contain 'userlogin'");
        assertTrue(responseContent.contains("User"), "Response does not contain 'User'");
        assertTrue(responseContent.contains("1990-01-01"), "Response does not contain '1990-01-01'");
    }

    @Test
    void getUser_ShouldReturnUser() throws Exception {
        User user = new User("user@example.com", "userlogin",
                "User", LocalDate.of(1990, 1, 1));
        user.setId(1L);

        when(userService.getUserById(1L)).thenReturn(java.util.Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.login").value("userlogin"));
    }


    @Test
    void getUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(userService.getUserById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        User user = new User("user@example.com", "userlogin",
                "UpdatedUser", LocalDate.of(1990, 1, 1));
        user.setId(1L);

        when(userService.updateUser(user)).thenReturn(user);

        // Сериализация объекта User в JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Подключает поддержку LocalDate

        String userJson = objectMapper.writeValueAsString(user);
        System.out.println("Serialized User JSON: " + userJson);

        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(userJson))  // Используем сериализованный JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedUser"));
    }


    @Test
    void deleteUser_ShouldReturnNoContent_WhenDeleted() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenNotDeleted() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(false);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }
    @Test
    void addFriend_ShouldAddFriend() throws Exception {
        User user1 = new User("user1@example.com", "user1login", "User1",
                LocalDate.of(1990, 1, 1));
        User user2 = new User("user2@example.com", "user2login", "User2",
                LocalDate.of(1992, 2, 2));

        user1.setId(1L);
        user2.setId(2L);

        when(userService.getUserById(1L)).thenReturn(java.util.Optional.of(user1));
        when(userService.getUserById(2L)).thenReturn(java.util.Optional.of(user2));
        when(userService.addFriend(1L, 2L)).thenReturn(true); // Мокируем результат добавления друга

        mockMvc.perform(post("/users/1/friends/2"))
                .andExpect(status().isOk());

        // Проверяем, что метод addFriend вызван
        verify(userService).addFriend(1L, 2L);
    }

    @Test
    void addFriend_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/1/friends/2"))
                .andExpect(status().isNotFound()); // Ожидаем статус 404
    }

    @Test
    void removeFriend_ShouldCallServiceAndReturnOk() throws Exception {
        User user1 = new User("user1@example.com", "user1login", "User1",
                LocalDate.of(1990, 1, 1));
        User user2 = new User("user2@example.com", "user2login", "User2",
                LocalDate.of(1992, 2, 2));

        user1.setId(1L);
        user2.setId(2L);

        when(userService.getUserById(1L)).thenReturn(java.util.Optional.of(user1));
        when(userService.getUserById(2L)).thenReturn(java.util.Optional.of(user2));

        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().isOk());

        // Проверка, что метод removeFriend вызван
        verify(userService).removeFriend(1L, 2L);
    }

}
