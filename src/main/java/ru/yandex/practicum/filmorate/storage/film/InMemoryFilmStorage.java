package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.DuplicationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> get() {
        log.info("Получен список фильмов.");
        return films.values();
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        for (Film value : films.values()) {
            if (film.getName().equals(value.getName())) {
                log.error("Такое название уже есть");
                throw new DuplicationException("Дублирование названия при добавлении");
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

    @Override
    public Film update(Film film) throws ValidationException {
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
        throw new NotFoundException(String.format("Фильм с id = %d  - не найден", film.getId()));
    }

    @Override
    public Collection<Film> getPopular(Long count) {
        return List.of();
    }

    @Override
    public boolean deleteFilmById(Long id) {
        return false;
    }

    @Override
    public Film findByName(String name) {
        return null;
    }

    @Override
    public Film getById(Long id) {
        log.debug("Выполняем поиск фильма в коллекции фильмов по id = {} ", id);
        return films.get(id);
    }

   /* @Override
    public void updateLikes(Film film) {
        if (films.containsKey(film.getId())) {
            Film filmStored = films.get(film.getId());
            filmStored.setLikes(film.getLikes());
            log.debug("Фильм с id = {} обновлён с новым количеством лайков", film.getId());
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }*/

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}