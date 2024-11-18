package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class InMemoryUserStorageTest {

    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;

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
        assertDoesNotThrow(() -> inMemoryUserStorage.create(user1), "Выкидывает исключение.");
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.create(user1), "Не выкидывает исключение.");
    }

    @Test
    void update() {
        assertThrows(NotFoundException.class, () -> inMemoryUserStorage.update(user3), "Не выкидывает исключение.");
        inMemoryUserStorage.create(user4);
        System.out.println(inMemoryUserStorage.findAll());
    }
}