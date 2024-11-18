package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InMemoryFilmStorageTest {

    @Autowired
    private InMemoryFilmStorage inMemoryFilmStorage;

    Film film1 = new Film("Прибы́тие по́езда на вокза́л Ла-Сьота",
            LocalDate.parse("06.01.1896", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            42);
    Film film2 = new Film("Фейк по́езда на вокза́л Ла-Сьота",
            LocalDate.parse("06.01.1894", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            45);

    @Test
    void addFilm() {
        assertDoesNotThrow(() -> inMemoryFilmStorage.create(film1), "Выкидывает исключение.");
        assertThrows(ValidationException.class, () -> inMemoryFilmStorage.create(film2), "Не выкидывает исключение.");
        assertEquals(1, inMemoryFilmStorage.findAll().size(), "Несоотвтетвующее количество сохраненных фильмов.");
    }

    @Test
    void update() {
        assertThrows(ValidationException.class, () -> inMemoryFilmStorage.update(film2), "Не выкидывает исключение.");
    }
}