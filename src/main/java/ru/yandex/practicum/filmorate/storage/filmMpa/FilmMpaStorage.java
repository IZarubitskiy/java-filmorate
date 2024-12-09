package ru.yandex.practicum.filmorate.storage.filmMpa;

import ru.yandex.practicum.filmorate.model.Mpa;

public interface FilmMpaStorage {

    void addFilmMpa(Long filmId, Long mpaId);

    Mpa getFilmMpaById(Long filmId);

    void deleteFilmMpaById(Long filmId);
}