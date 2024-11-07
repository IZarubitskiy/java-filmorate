package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        for (Film value : films.values()) {
            if (film.getName().equals(value.getName())) {
                log.error("Такое название уже есть");
                throw new ValidationException("Дублирование названия при добавлении");
            }
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("При добавлении выбрана не соответствующая дата фильма.");
            throw new ValidationException("Выбрана дата до 28 декабря 1895 года.");
        }

        film.setId(getNextId());
        log.debug("Фильму \"{}\" назначен id = {}", film.getName(), film.getId());
        films.put(film.getId(), film);
        log.info("Фильм \"{}\" с id = {} - добавлен", film.getName(), film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getId() == null) {
            log.error("Id не указан");
            throw new ValidationException("Id должен быть указан");

        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("При обновлении выбрана не соответствующая дата фильма.");
            throw new ValidationException("Выбрана дата до 28 декабря 1895 года.");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм \"{}\" с id = {} - обновлен", film.getName(), film.getId());
            return film;
        }
        log.error("Попытка получить фильм с несуществующим id = {}", film.getId());
        throw new ValidationException(String.format("Фильм с id = %d  - не найден", film.getId()));
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.info("Получен список фильмов.");
        return films.values();
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
