package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Long userId, Long friendId) {
        final String sql = "insert into friendships (user_id, friend_id, confirmation) values (?, ?, ?)";
        log.info("Пользователь с id = {} добавил в друзья пользователя с id = {}.", userId, friendId);
        jdbcTemplate.update(sql, userId, friendId, false);
    }

    @Override
    public void remove(Long userId, Long friendId) {
        final String sql = "delete from friendships where user_id = ? and friend_id = ?";
        log.info("Пользователь с id = {} удалил из друзей пользователя с id = {}.", userId, friendId);
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void confirm(Long userId, Long friendId) {
        final String sql = "update friendships set confirmation = ? where user_id = ? and friend_id = ?";
        log.info("Пользователь с id = {} подтвердил дружбу с пользователем с id = {}.", userId, friendId);
        jdbcTemplate.update(sql, true, userId, friendId);

    }
}