package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя с id = {}", user.getId());
        return userService.updateUser(user);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> findAll() {
        log.info("Запрос на получение всех пользователей");
        return ResponseEntity.ok(userService.getAllUsers());  // Возвращаем всех пользователей
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        return userService.getUserById((long) id)
                .map(ResponseEntity::ok)  // Если пользователь найден, возвращаем 200 OK
                .orElse(ResponseEntity.notFound().build());  // Если нет, возвращаем 404
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (userService.deleteUser((long) id)) {
            return ResponseEntity.noContent().build();  // Если пользователь удален, возвращаем 204 No Content
        }
        return ResponseEntity.notFound().build();  // Если нет, возвращаем 404 Not Found
    }
}