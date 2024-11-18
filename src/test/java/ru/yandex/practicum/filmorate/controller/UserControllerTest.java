package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    UserController userController;

    User user1 = new User("karamba@bat.com",
            "ElBarto",
            LocalDate.parse("23.02.1979", DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    User user3 = new User("saxgirl@bat.com",
            "LizaS",
            LocalDate.parse("23.02.1979", DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    User user4 = new User("saxgirlbat.com",
            "LizaS",
            LocalDate.parse("23.02.1979", DateTimeFormatter.ofPattern("dd.MM.yyyy")));

    @Test
    void create() {
        assertDoesNotThrow(() -> userController.create(user1), "Выкидывает исключение.");
        assertThrows(ValidationException.class, () -> userController.create(user1), "Не выкидывает исключение.");
    }

    @Test
    void update() {
        userController.create(user1);
        assertThrows(ValidationException.class, () -> userController.update(user3), "Не выкидывает исключение.");
        userController.create(user4);
        System.out.println(userController.findAllUsers());
    }
}