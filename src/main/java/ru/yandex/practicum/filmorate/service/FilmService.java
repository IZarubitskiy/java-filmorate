package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
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
    private final String msgUser = "Пользователь не найден";
    private final String msgFilm = "Фильм не найден";

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public Optional<Film> findById(Long id) {
        return filmStorage.findById(id);
    }

    public Film like(Long filmId, Long userId) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException(msgUser));
        Film film = filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException(msgFilm));

        if (user.getLikedFilms().contains(filmId)) {
            log.error("Повторное добавление лайка");
            throw new ValidationException("Фильм уже в любимых.");
        }

        user.getLikedFilms().add(filmId);
        log.debug("Добавление в список любимых фильмов пользователя с id = {} фильма с id = {}.", userId, filmId);
        film.increaseLikes();
        log.debug("Увеличение количества лайков фильма с id = {}.", filmId);
        filmStorage.updateLikes(film);
        log.debug("Обновление количества лайков фильма с id= {}. в базе фильмов", filmId);
        userStorage.updateFriends(user);
        log.debug("Обновление списка любимых фильмов пользователя с id= {} в базе пользователей, лайк.", userId);
        log.info("Пользователь с id = {} поставил лайк фильму с id = {}.", userId, filmId);
        return film;
    }

    public Film unlike(Long filmId, Long userId) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException(msgUser));
        Film film = filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException(msgFilm));

        if (user.getLikedFilms().contains(filmId)) {
            user.getLikedFilms().remove(filmId);
            log.debug("Удаление их списка любимых фильмов пользователя с id = {} фильма с id = {}.", userId, filmId);
            film.decreaseLikes();
            log.debug("Уменьшение количества лайков фильма с id = {}.", filmId);
            userStorage.updateFriends(user);
            log.debug("Обновление списка любимых фильмов пользователя с id= {} в базе пользователей, дизлайк.", userId);
            filmStorage.updateLikes(film);
            log.debug("Обновление фильма с id= {} в базе фильмов, после удаления лайка фильму.", userId);
            log.info("Пользователем с id = {} был удален лайк фильму \"{}\"", user.getName(), film.getName());
        } else {
            log.error("Удаление отсутствующего фильма из любимых");
            throw new ValidationException("Фильма нет в любимых.");
        }
        return film;
    }

    public Collection<Film> getPopular(int count) {

         Collection<Film> popularFilms = filmStorage.findAll()
                .stream()
                .sorted((o1, o2) -> {
                    if (o1.getLikes() > o2.getLikes()) {
                        return -1;
                    } else if (o1.getLikes() < o2.getLikes()) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                 .limit(count)
                .collect(Collectors.toList());
        log.debug("Получена коллекция фильмов начиная с 1 и до {} ", count);
        if (popularFilms.isEmpty()) {
            log.error("Попытка получить пустой список популярных фильмов");
            throw new NotFoundException("Список популярных фильмов пуст");
        }
        log.info("Список популярных фильмов получен пользователем, начиная с 1 и до {}", count);
        return popularFilms;
    }
}
