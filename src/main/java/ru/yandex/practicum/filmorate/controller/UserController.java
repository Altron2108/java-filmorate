package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Valid;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {

    private final List<User> users = new ArrayList<>();
    private int currentId = 1;

    // Создание пользователя
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        user.setId(currentId++);
        users.add(user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    // Обновление пользователя
    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @Valid @RequestBody User user) {
        User existingUser = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));

        existingUser.setEmail(user.getEmail());
        existingUser.setLogin(user.getLogin());
        existingUser.setName(user.getName());
        existingUser.setBirthday(user.getBirthday());

        log.info("Пользователь обновлен: {}", existingUser);
        return existingUser;
    }

    // Удаление пользователя
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        User existingUser = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));

        users.remove(existingUser);
        log.info("Пользователь с ID {} удален", id);
    }

    // Получение всех пользователей
    @GetMapping
    public List<User> getUsers() {
        log.info("Запрос на получение всех пользователей");
        return users;
    }
}
