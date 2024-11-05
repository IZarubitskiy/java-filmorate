package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {

    FilmController filmController = new FilmController();
    Film film1 = new Film("Прибы́тие по́езда на вокза́л Ла-Сьота",
            LocalDate.parse("06.01.1896", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            Duration.ofSeconds(48));
    Film film2 = new Film("Фейк по́езда на вокза́л Ла-Сьота",
            LocalDate.parse("06.01.1894", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            Duration.ofSeconds(48));

    @Test
    void customFilmValidator() {
        assertDoesNotThrow(() -> filmController.customFilmValidator(film1), "Выкидывает исключение.");
        assertThrows(ValidationException.class, () -> filmController.customFilmValidator(film2), "Не выкидывает исключение.");
    }

    @Test
    void updateFilm() {
        filmController.addFilm(film1);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film2), "Не выкидывает исключение.");


    }
}