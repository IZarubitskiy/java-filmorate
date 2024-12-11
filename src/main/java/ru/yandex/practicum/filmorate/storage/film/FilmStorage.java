package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film addFilm(Film film);

    Film getFilmById(Long id);

    Collection<Film> get();

    Film update(Film film);

    Collection<Film> getPopular(Long count, Long genreId, Long year);

    boolean deleteFilmById(Long id);

    Film findByName(String name);

}