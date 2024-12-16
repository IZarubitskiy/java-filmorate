package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre getGenreById(Long id) {
        Genre genre = genreStorage.getGenreById(id);
        checkGenreIsNotNull(genre, id);
        log.info("Получен Жанр по Id: {}", id);
        return genre;
    }

    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    private void checkGenreIsNotNull(Genre genre, Long id) {
        if (Objects.isNull(genre) || Objects.isNull(getAllGenres().stream()
                .collect(Collectors.toMap(Genre::getId, g -> g)).get(genre.getId()))) {
            throw new NotFoundException(String.format("Жанра с id %s нет", id));
        }
    }
}