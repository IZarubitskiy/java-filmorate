package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final String genresSql = "select * from genres";

    @Override
    public Genre getGenreById(Integer genreId) {
        try {
            log.info("Жанр с id = {} найден.", genreId);
            return jdbcTemplate.queryForObject(genresSql.concat(" where id = ?"), new GenreMapper(), genreId);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<Genre> getAllGenres() {
        log.info("Получен список Жанров.");
        return jdbcTemplate.query(genresSql, new GenreMapper());
    }
}