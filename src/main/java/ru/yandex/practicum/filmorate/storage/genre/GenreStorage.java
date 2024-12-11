package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    Genre getGenreById(Long genreId);

    Collection<Genre> getAllGenres();

    //    DROP TABLE film_mpas , film_genres , likes , films , mpas , users , genres , friendships CASCADE;
}