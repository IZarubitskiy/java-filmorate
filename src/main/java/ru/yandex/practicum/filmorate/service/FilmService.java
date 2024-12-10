package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    @Autowired
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    @Autowired
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    private final String  NOT_FOUND_USER = "Пользователь не найден";
    private final String  NOT_FOUND_FILM  = "Фильм не найден";

    public Collection<Film> get() {
        return filmStorage.get();
    }

    public Film add(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("При добавлении выбрана не соответствующая дата фильма.");
            throw new ValidationException("Выбрана дата до 28 декабря 1895 года.");
        }

        if (filmStorage.findByName(film.getName()) != null) {
            throw new DuplicationException("Дублирование названия при добавлении");
        }

        return filmStorage.add(film);
    }

    public Film update(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Выбрана дата до 28 декабря 1895 года.");
        }

        if (filmStorage.getById(film.getId()) == null) {
            throw new DuplicationException(NOT_FOUND_FILM);
        }

        return filmStorage.update(film);
    }

    public Film getById(Long id) {
        return filmStorage.getById(id);
    }

    public void addLikeToFilm(Long filmId, Long userId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        if (filmStorage.getById(filmId) == null) {
            throw new DuplicationException(NOT_FOUND_FILM);
        }

        likeStorage.addLikeToFilm(filmId, userId);
    }

    public void deleteLikeFromFilm(Long filmId, Long userId) {

        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        if (filmStorage.getById(filmId) == null) {
            throw new DuplicationException(NOT_FOUND_FILM);
        }

        likeStorage.deleteLikeFromFilm(filmId, userId); }

    public Collection<Film> getPopular(Long count) {
        return filmStorage.getPopular(count);
    }

    public void deleteFilmById(Long id) {
        if (!filmStorage.deleteFilmById(id)) {
            throw new NotFoundException(String.format(NOT_FOUND_FILM, id));
        }
    }
}