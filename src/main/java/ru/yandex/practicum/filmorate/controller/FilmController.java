package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        try {
            customFilmValidator(film);
        } catch (ValidationException e) {
            log.error("Валидация не пройдена", e);
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("Фильм добавлен.", film);
        return film;
    }

    @PutMapping
    public void updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        try {
            customFilmValidator(film);
        } catch (ValidationException e) {
            log.error("Валидация не пройдена", e);
        }

        OptionalLong idFilm = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .filter(id -> film.equals(films.get(id)))
                .findFirst();

        if (idFilm.isEmpty()) {
            log.error("Валидация не пройдена", new ValidationException("Фильм, который необходимо обновить, не существует."));
            throw new ValidationException("Фильм, который необходимо обновить, не существует.");
        }

        long id = idFilm.getAsLong();
        films.put(id, film);
        log.debug("Фильм обновлен.", film);

    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.trace("Фильмы получены.", films.values());
        return films.values();

    }

    public void customFilmValidator(Film film) throws ValidationException {
        if (film.getName().isBlank() ||
                film.getName() == null) {
            throw new ValidationException("Фильм без названия");
        }
        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                throw new ValidationException("Описание содержит более 200 символов.");
            }
        }
        if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма отрицательная.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("28.12.1895", DateTimeFormatter.ofPattern("dd.MM.yyyy")))) {
            throw new ValidationException("Выбрана дата до 28 декабря 1895 года.");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
