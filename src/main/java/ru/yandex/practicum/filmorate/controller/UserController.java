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
import java.util.OptionalLong;

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
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен.", user);
        return user;
    }

    @PutMapping
    public void updateUser(@Valid @RequestBody User user) throws ValidationException {

        try {
            customUserValidator(user);
        } catch (ValidationException e) {
            log.error("Валидация не пройдена", e);
            return;
        }

        OptionalLong idUser = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .filter(id -> user.equals(users.get(id)))
                .findFirst();

        if (idUser.isEmpty()) {
            log.error("Валидация не пройдена", new ValidationException("Пользователь, которого необходимо обновить, не существует."));
            throw new ValidationException("Пользователь, которого необходимо обновить, не существует.");
        }

        long id = idUser.getAsLong();
        users.put(id, user);
        log.debug("Пользователь обновлен.", user);

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
