package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        log.info("Adding new user: {}", user);
        User createdUser = userStorage.addUser(user);
        log.info("User added successfully with ID: {}", createdUser.getId());
        return createdUser;
    }

    public Optional<User> getUserById(long id) {
        log.info("Fetching user with ID: {}", id);
        Optional<User> user = userStorage.getUserById(id);
        if (user.isPresent()) {
            log.info("User found: {}", user.get());
        } else {
            log.warn("User with ID {} not found", id);
        }
        return user;
    }

    public User updateUser(User user) {
        log.info("Updating user with ID: {}", user.getId());
        User updatedUser = userStorage.updateUser(user);
        log.info("User updated successfully: {}", updatedUser);
        return updatedUser;
    }

    public boolean deleteUser(long id) {
        log.info("Deleting user with ID: {}", id);
        boolean isDeleted = userStorage.deleteUser(id);
        if (isDeleted) {
            log.info("User with ID {} deleted successfully", id);
        } else {
            log.warn("Failed to delete user with ID {}. User not found.", id);
        }
        return isDeleted;
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userStorage.getAllUsers();
        log.info("{} users found", users.size());
        return users;
    }
}
