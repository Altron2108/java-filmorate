package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        log.info("Запрос на добавление нового пользователя: {}", user);
        User createdUser = userStorage.addUser(user);
        log.info("Пользователь успешно добавлен. Присвоен ID: {}", createdUser.getId());
        return createdUser;
    }

    public Optional<User> getUserById(long id) {
        log.info("Запрос на получение данных пользователя с ID: {}", id);
        Optional<User> user = userStorage.getUserById(id);
        if (user.isPresent()) {
            log.info("Пользователь найден: {}", user.get());
        } else {
            log.warn("Пользователь с ID {} не найден", id);
        }
        return user;
    }

    public User updateUser(User user) {
        log.info("Запрос на обновление данных пользователя с ID: {}", user.getId());
        User updatedUser = userStorage.updateUser(user);
        log.info("Данные пользователя успешно обновлены: {}", updatedUser);
        return updatedUser;
    }

    public boolean deleteUser(long id) {
        log.info("Запрос на удаление пользователя с ID: {}", id);
        boolean isDeleted = userStorage.deleteUser(id);
        if (isDeleted) {
            log.info("Пользователь с ID {} успешно удалён", id);
        } else {
            log.warn("Удаление невозможно: пользователь с ID {} не найден", id);
        }
        return isDeleted;
    }

    public List<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей");
        List<User> users = userStorage.getAllUsers();
        log.info("Найдено {} пользователей", users.size());
        return users;
    }

    public boolean addFriend(long userId, long friendId) {
        log.info("Запрос на добавление друга с ID {} для пользователя с ID {}", friendId, userId);
        User user = userStorage.getUserById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        User friend = userStorage.getUserById(friendId).orElseThrow(() ->
                new NotFoundException("Друг не найден"));

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info("Друг с ID {} успешно добавлен пользователю с ID {}", friendId, userId);
        return false;
    }

    public void removeFriend(long userId, long friendId) {
        log.info("Запрос на удаление друга с ID {} у пользователя с ID {}", friendId, userId);
        User user = userStorage.getUserById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        User friend = userStorage.getUserById(friendId).orElseThrow(() ->
                new NotFoundException("Друг не найден"));

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info("Друг с ID {} успешно удалён у пользователя с ID {}", friendId, userId);
    }

    public List<User> getFriends(long userId) {
        log.info("Запрос на получение списка друзей пользователя с ID {}", userId);
        User user = userStorage.getUserById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        List<User> friends = user.getFriends().stream()
                .map(friendId -> userStorage.getUserById(friendId).orElseThrow(() ->
                        new NotFoundException("Друг не найден")))
                .collect(Collectors.toList());
        log.info("Найдено {} друзей для пользователя с ID {}", friends.size(), userId);
        return friends;
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        log.info("Запрос на получение общих друзей пользователей с ID {} и {}", userId, otherId);
        User user = userStorage.getUserById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        User other = userStorage.getUserById(otherId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));

        List<User> commonFriends = user.getFriends().stream()
                .filter(other.getFriends()::contains)
                .map(friendId -> userStorage.getUserById(friendId).orElseThrow(() ->
                        new NotFoundException("Друг не найден")))
                .collect(Collectors.toList());
        log.info("Найдено {} общих друзей для пользователей с ID {} и {}",
                commonFriends.size(), userId, otherId);
        return commonFriends;
    }
}
