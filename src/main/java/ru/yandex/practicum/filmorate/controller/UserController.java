package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController

@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        for (User value : users.values()) {
            if (user.getEmail().equals(value.getEmail())) {
                log.error(String.format("Email %s уже существует.", user.getEmail()), new ValidationException("Необходим новый Email при добавлении."));
                throw new ValidationException("Необходим новый Email при добавлении.");
            }
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Вместо имени использован логин при Добавлении");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен.");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Вместо имени использован логин при Обновлении");
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь обновлен.");
            return user;
        }
        log.error("Попытка обновить пользователя с несуществующим id = {}", user.getId());
        throw new ValidationException(String.format("Пользователь с id = %d  - не найден", user.getId()));
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
