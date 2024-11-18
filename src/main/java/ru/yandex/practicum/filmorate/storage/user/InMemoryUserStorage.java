package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        log.info("Получен список Пользователей.");
        return users.values();
    }

    @Override
    public User create(User user) throws ValidationException {
        for (User value : users.values()) {
            if (user.getEmail().equals(value.getEmail())) {
                log.error(String.format("Email %s уже существует.", user.getEmail()));
                throw new ValidationException("Необходим новый Email при добавлении.");
            }
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Вместо имени использован логин при Добавлении");
        }
        user.setId(getNextId());
        log.debug("Пользователю \"{}\" назначен id = {}", user.getName(), user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен.");
        return user;
    }

    @Override
    public User update(User user) throws ValidationException {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Вместо имени использован логин при Обновлении");
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь обновлен.");
            return user;
        }
        log.error("Попытка обновить пользователя с несуществующим id = {}", user.getId());
        throw new NotFoundException(String.format("Пользователь с id = %d  - не найден", user.getId()));
    }

    @Override
    public Optional<User> findById(Long id) {
        log.debug("Выполняем поиск пользователя по id = {} ", id);
        return Optional.ofNullable(users.get(id));
    }

    public void updateFriends(User user) {
        if (users.containsKey(user.getId())) {
            User userStored = users.get(user.getId());
            userStored.setFriends(user.getFriends());
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void updateLikes(User user) {
        if (users.containsKey(user.getId())) {
            User userStored = users.get(user.getId());
            userStored.setLikedFilms(user.getLikedFilms());
            log.debug("Фильм с id = {} обновлён с новым количеством лайков", user.getId());
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
