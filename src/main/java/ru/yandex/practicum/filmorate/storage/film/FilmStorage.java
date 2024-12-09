package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film add(Film film);

    Film getById(Long id);

    Collection<Film> get();

    Film update(Film film);

    Collection<Film> getPopular(Integer count, Integer genreId, Integer year);

    boolean deleteFilmById(Long id);

}