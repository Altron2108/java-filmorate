package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final List<User> users = new CopyOnWriteArrayList<>();

    public User createUser(User user) {
        validateUser(user);
        user.setId(idCounter.getAndIncrement());
        users.add(user);
        return user;
    }

    public Optional<User> getUserById(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst();
    }

    public Optional<User> updateUser(int id, User user) {
        return getUserById(id).map(existingUser -> {
            if (user.getName() != null && !user.getName().isBlank()) {
                existingUser.setName(user.getName());
            }
            if (user.getEmail() != null && !user.getEmail().isBlank()) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getLogin() != null && !user.getLogin().isBlank()) {
                existingUser.setLogin(user.getLogin());
            }
            if (user.getBirthday() != null) {
                existingUser.setBirthday(user.getBirthday());
            }
            return existingUser;
        });
    }

    public boolean deleteUser(int id) {
        return users.removeIf(user -> user.getId() == id);
    }

    public List<User> getAllUsers() {
        return users.stream().map(u -> new User(u.getEmail(), u.getLogin(), u.getName(), u.getBirthday())).toList();
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email не может быть пустым.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new IllegalArgumentException("Логин не может быть пустым.");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Дата рождения должна быть в прошлом.");
        }

    }
}