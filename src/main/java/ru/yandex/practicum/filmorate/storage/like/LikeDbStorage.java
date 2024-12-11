package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLikeToFilm(Long filmId, Long userId) {
        final String sql = "insert into likes (film_id, user_id) values (?, ?)";

        try {
            jdbcTemplate.update(sql, filmId, userId);
        } catch (DuplicateKeyException ignored) {
            log.warn("Пользователь с id = {} уже ставил лайк фильму с id = {}", userId, filmId);
        }
    }

    @Override
    public void deleteLikeFromFilm(Long filmId, Long userId) {
        final String sql = "delete from likes where film_id = ? and user_id = ?";
        log.info("Пользователь с id = {} удалил лайк фильму с id = {}", userId, filmId);
        jdbcTemplate.update(sql, filmId, userId);
    }
}