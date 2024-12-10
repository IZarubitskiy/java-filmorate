package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    private final String msgUser = "Пользователь не найден";
    private final String msgFilm = "Фильм не найден";

    public Collection<Film> findAll() {
        return filmStorage.get();
    }

    public Film create(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public Film findById(Long id) {
        return filmStorage.getById(id);
    }

    public void like(Long filmId, Long userId) { likeStorage.addLikeToFilm(filmId, userId); }

    public void unlike(Long filmId, Long userId) { likeStorage.deleteLikeFromFilm(filmId, userId); }

    public Collection<Film> getPopular(Long count, Long genreId, Long year) {
        return filmStorage.getPopular(count, genreId,year ); }
}