package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final List<User> users = new ArrayList<>();

    public User createUser(User user) { // Возвращает User без Optional
        user.setId(idCounter.getAndIncrement());
        users.add(user);
        return user;
    }

    public Optional<User> getUserById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    public Optional<User> updateUser(int id, User user) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    return existingUser;
                });
    }

    public boolean deleteUser(int id) {
        return users.removeIf(user -> user.getId() == id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
