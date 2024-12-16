package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {

    void addLikeToFilm(Long filmId, Long userId);

    void deleteLikeFromFilm(Long filmId, Long userId);
}