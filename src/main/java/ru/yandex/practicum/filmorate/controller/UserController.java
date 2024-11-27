package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ApiResponse;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserStorage userStorage;

    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя с данными: {}", user);

        // Пример валидации, аналогичный фильму. Например, проверим, что день рождения пользователя не в будущем
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения пользователя не может быть в будущем");
        }

        log.info("Создание нового пользователя: {}", user.getLogin());
        User createdUser = userStorage.addUser(user);
        log.info("Пользователь создан: {}", createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);  // Возвращаем созданный объект
    }

    @PutMapping
    public ResponseEntity<ApiResponse<User>> update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя с id = {}", user.getId());

        if (user.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ID пользователя не может быть пустым", null));
        }

        Optional<User> updatedUser = userStorage.updateUser(user);
        return updatedUser.map(value ->
                ResponseEntity.ok(new ApiResponse<>("Пользователь успешно обновлен", value))).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("Пользователь с id = " + user.getId() + " не найден", null)));
    }



    @GetMapping
    public ResponseEntity<Collection<User>> findAll() {
        log.info("Запрос на получение всех пользователей");
        return ResponseEntity.ok(userStorage.getAllUsers());  // Возвращаем всех пользователей
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        return userStorage.getUserById((long) id)
                .map(ResponseEntity::ok)  // Если пользователь найден, возвращаем 200 OK
                .orElse(ResponseEntity.notFound().build());  // Если нет, возвращаем 404
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (userStorage.deleteUser((long) id)) {
            return ResponseEntity.noContent().build();  // Если пользователь удален, возвращаем 204 No Content
        }
        return ResponseEntity.notFound().build();  // Если нет, возвращаем 404 Not Found
    }
}
