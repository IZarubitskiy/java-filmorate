package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Film add(Film film);

    Film getById(Long id);

    Collection<Film> get();

    Film update(Film film);

    Collection<Film> getPopular(Long count);

    boolean deleteFilmById(Long id);

    Film findByName(String name);

}