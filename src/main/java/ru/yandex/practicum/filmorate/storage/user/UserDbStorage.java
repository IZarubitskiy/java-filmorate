package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage{
    private static final String USERS_SQL = "select * from users";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> get(){
        log.info("Получен список Пользователей.");
        return jdbcTemplate.query(USERS_SQL, new UserMapper());
    }

    @Override
    public User add(User user){
        final String sql = "insert into users (name, login, birthday, email) values (?, ?, ?, ?)";
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setObject(3, user.getBirthday());
            preparedStatement.setString(4, user.getEmail());

            return preparedStatement;
        }, generatedKeyHolder);

        Long userId = Objects.requireNonNull(generatedKeyHolder.getKey()).longValue();

        user.setId(userId);
        log.info("Пользователь \"{}\" с id = {} добавлен.", user.getName(), user.getId());
        return user;
    }

    @Override
    public User update(User user){
        final String sql = "update users set name = ?, login = ?, birthday = ?, email = ? where id = ?";

        jdbcTemplate.update(
                sql,
                user.getName(), user.getLogin(), user.getBirthday(), user.getEmail(), user.getId()
        );
        log.info("Пользователь \"{}\" с id = {} обновлен.", user.getName(), user.getId());
        return user;
    }

    @Override
    public User getById(Long id){
        try {
            return jdbcTemplate.queryForObject(USERS_SQL.concat(" where id = ?"), new UserMapper(), id);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<User> getFriends(Long id){
        final String sql = "select * from users where id in (select f.friend_id from users u join friendships f " +
                "on u.id = f.user_id where u.id = ?)";
        log.info("Получен список друзей пользователя с id = {}", id);
        return jdbcTemplate.query(sql, new UserMapper(), id);
    }

    @Override
    public Collection<User> getCommonFriends(Long user1Id, Long user2Id){
        final String sql = "select * from users where id in (select friend_id from users u join friendships f on " +
                "u.id = f.user_id where u.id = ?) and id in (select friend_id from users u join friendships f on " +
                "u.id = f.user_id where u.id = ?)";
        log.info("Получен список  общих друзей пользователей с id : {} и  {}.", user1Id , user2Id);
        return jdbcTemplate.query(sql, new UserMapper(), user1Id, user2Id);
    }

    @Override
    public boolean deleteUserById(Long id){
        final String sql = "delete from users where id = ?";
        int status = jdbcTemplate.update(sql, id);
        log.info("Пользователь с id = {} удален.", id);
        return status != 0;
    }

    @Override
    public boolean contains(Long id ){
        try {
            getById(id);
            log.info("Найден пользователь ID_{}.", id);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.info("Не найден пользователь ID_{}.", id);
            return false;
        }
    }
}
