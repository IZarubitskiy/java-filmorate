package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    private final String msgUser = "Пользователь не найден";
    private final String msgFilm = "Фильм не найден";

    @Autowired
    public FilmService(@Qualifier("userDbStorage") UserStorage userStorage, @Qualifier("filmDbStorage") FilmStorage filmStorage, LikeStorage likeStorage, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public Collection<Film> get() {
        return filmStorage.get();
    }

    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("При добавлении выбрана не соответствующая дата фильма.");
            throw new ValidationException("Выбрана дата до 28 декабря 1895 года.");
        }
/*
        if (filmStorage.findByName(film.getName()) != null) {
            throw new DuplicationException("Дублирование названия при добавлении");
        }*/
        if (film.getMpa().getId() > mpaStorage.getAllMpa().size()) {
            throw new ValidationException("точно зда");
        }

        for (Genre g : film.getGenres()) {
            if (g.getId() > genreStorage.getAllGenres().size()) {
                throw new ValidationException("точно зда");
            }
        }

        return filmStorage.addFilm(film);
    }

    public Film update(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Выбрана дата до 28 декабря 1895 года.");
        }
        return filmStorage.update(film);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public void addLikeToFilm(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new DuplicationException(msgFilm);
        }

        likeStorage.addLikeToFilm(filmId, userId);
    }

    public void deleteLikeFromFilm(Long filmId, Long userId) {

        if (filmStorage.getFilmById(filmId) == null) {
            throw new DuplicationException(msgFilm);
        }
        likeStorage.deleteLikeFromFilm(filmId, userId);
    }

    public Collection<Film> getPopular(Long count) {
        return filmStorage.getPopular(count);
    }

    public void deleteFilmById(Long id) {
        if (!filmStorage.deleteFilmById(id)) {
            throw new NotFoundException(String.format(msgFilm, id));
        }
    }
}