package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController

@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {

        try {
            customUserValidator(user);
        } catch (ValidationException e) {
            log.error("Валидация не пройдена", e);
            return null;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Вместо имени использован логин при добавлении");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен.", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (user.getEmail().isBlank() ||
                user.getEmail() == null) {
            throw new ValidationException("Пустой email");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Вместо имени использован логин при Обновлении");
        }
        if (users.containsKey(user.getId())) {
            // long id = idUser.getAsLong();
            users.put(user.getId(), user);
            log.debug("Пользователь обновлен.", user);
            return user;
        }
        log.error("Попытка обновить пользователя с несуществующим id = {}", user.getId());
        throw new ValidationException(String.format("Пользователь с id = %d  - не найден", user.getId()));
    }


    @GetMapping
    public Collection<User> findAllUserss() {
        return users.values();
    }

    public void customUserValidator(User user) throws ValidationException {
        if (user.getEmail().isBlank() ||
                user.getEmail() == null) {
            throw new ValidationException("Пустой email");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email не содержит @.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getLogin() == null
                || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }
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
