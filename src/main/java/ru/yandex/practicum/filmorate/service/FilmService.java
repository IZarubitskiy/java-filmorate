package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.filmMpa.FilmMpaStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Service
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final FilmMpaStorage filmMpaStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final String msgUser = "Пользователь не найден";
    private final String msgFilm = "Фильм не найден";

    @Autowired
    public FilmService(@Qualifier("userDbStorage") UserStorage userStorage, @Qualifier("filmDbStorage") FilmStorage filmStorage, LikeStorage likeStorage, MpaStorage mpaStorage, GenreStorage genreStorage, FilmMpaStorage filmMpaStorage, FilmGenreStorage filmGenreStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.filmMpaStorage = filmMpaStorage;
        this.filmGenreStorage = filmGenreStorage;
    }

    public Collection<Film> get() {
        return setFilmGenres(filmStorage.get());
    }

    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("При добавлении выбрана не соответствующая дата фильма.");
            throw new ValidationException("Выбрана дата до 28 декабря 1895 года.");
        }

        if (film.getMpa().getId() > mpaStorage.getAllMpa().size()) {
            throw new ValidationException("Рейтинга нет в Базе данных");
        }

        for (Genre g : film.getGenres()) {
            if (g.getId() > genreStorage.getAllGenres().size()) {
                throw new ValidationException("Жанра нет в Базе данных.");
            }
        }

        return addExtraFields(filmStorage.addFilm(film));
    }

    public Film update(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Выбрана дата до 28 декабря 1895 года.");
        }

        filmMpaStorage.deleteFilmMpaById(film.getId());
        filmGenreStorage.deleteAllFilmGenresById(film.getId());

        return addExtraFields(filmStorage.update(film));
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException(msgFilm));
        film.setGenres(filmGenreStorage.getAllFilmGenresById(id));
        return film;
    }

    public void addLikeToFilm(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException(msgFilm));
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(msgUser));

        likeStorage.addLikeToFilm(filmId, userId);
    }

    public void deleteLikeFromFilm(Long filmId, Long userId) {

        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException(msgFilm));
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(msgUser));

        likeStorage.deleteLikeFromFilm(filmId, userId);
    }

    public Collection<Film> getPopular(Long count, Long genreId, Long year) {
        return setFilmGenres(filmStorage.getPopular(count, genreId, year));
    }

    public void deleteFilmById(Long id) {
        if (!filmStorage.deleteFilmById(id)) {
            throw new NotFoundException(msgFilm);
        }
    }

    private Film addExtraFields(Film film) {

        Long filmId = film.getId();
        Long mpaId = film.getMpa().getId();

        filmMpaStorage.addFilmMpa(filmId, mpaId);
        film.getGenres().forEach(genre -> filmGenreStorage.addFilmGenre(filmId, genre.getId()));

        Mpa filmMpa = mpaStorage.getMpaById(mpaId);
        Collection<Genre> filmGenres = filmGenreStorage.getAllFilmGenresById(filmId);

        return film.toBuilder().mpa(filmMpa).genres(filmGenres).build();
    }

    private Collection<Film> setFilmGenres(Collection<Film> films) {
        Map<Long, Collection<Genre>> filmGenresMap = filmGenreStorage.getAllFilmGenres(films);

        films.forEach(film -> {
            Long filmId = film.getId();

            film.setGenres(filmGenresMap.getOrDefault(filmId, new ArrayList<>()));
        });

        return films;
    }
}