package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        log.info("Creating new user: {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Updating user with ID: {}", user.getId());
        return userService.updateUser(user);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> findAll() {
        log.info("Fetching all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        log.info("Fetching user with ID: {}", id);
        return userService.getUserById((long) id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        log.info("Deleting user with ID: {}", id);
        if (userService.deleteUser((long) id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Adding friend with ID: {} to user with ID: {}", friendId, id);
        userService.addFriend((long) id, (long) friendId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        if (userService.addFriend(userId, friendId)) {
            return ResponseEntity.ok().build(); // Возвращаем 200 OK, если друг успешно добавлен
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Или 400 Bad Request, если операция не удалась
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Removing friend with ID: {} from user with ID: {}", friendId, id);
        userService.removeFriend((long) id, (long) friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Collection<User>> getFriends(@PathVariable int id) {
        log.info("Fetching friends for user with ID: {}", id);
        return ResponseEntity.ok(userService.getFriends((long) id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<User>> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Fetching common friends between user with ID: {} and user with ID: {}", id, otherId);
        return ResponseEntity.ok(userService.getCommonFriends((long) id, (long) otherId));
    }
}
