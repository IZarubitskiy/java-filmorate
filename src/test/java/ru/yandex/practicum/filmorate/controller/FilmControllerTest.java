package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController = new FilmController();
    Film film1 = new Film("Прибы́тие по́езда на вокза́л Ла-Сьота",
            LocalDate.parse("06.01.1896", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            42);
    Film film2 = new Film("Фейк по́езда на вокза́л Ла-Сьота",
            LocalDate.parse("06.01.1894", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            45);

    @Test
    void addFilm() {
        assertDoesNotThrow(() -> filmController.addFilm(film1), "Выкидывает исключение.");
        assertThrows(ValidationException.class, () -> filmController.addFilm(film2), "Не выкидывает исключение.");
        assertEquals(1,filmController.findAllFilms().size(), "Несоотвтетвующее количество сохраненных фильмов.");
    }

    @Test
    void updateFilm() {

        filmController.addFilm(film1);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film2), "Не выкидывает исключение.");


    }
}