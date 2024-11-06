package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    UserController userController = new UserController();

    User user1 = new User("karamba@bat.com",
            "ElBarto",
            LocalDate.parse("23.02.1979", DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    User user2 = new User("", "", LocalDate.parse("23.02.1979", DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    User user3 = new User("saxgirl@bat.com",
            "LizaS",
            LocalDate.parse("23.02.1979", DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    User user4 = new User("saxgirlbat.com",
            "LizaS",
            LocalDate.parse("23.02.1979", DateTimeFormatter.ofPattern("dd.MM.yyyy")));

    @Test
    void addUser() {
        assertDoesNotThrow(() -> userController.addUser(user1), "Выкидывает исключение.");
        assertThrows(ValidationException.class, () -> userController.addUser(user1), "Не выкидывает исключение.");
    }

    @Test
    void updateUser() {
        userController.addUser(user1);
        assertThrows(ValidationException.class, () -> userController.updateUser(user3), "Не выкидывает исключение.");
        userController.addUser(user4);
        System.out.println(userController.findAllUserss());
    }
}