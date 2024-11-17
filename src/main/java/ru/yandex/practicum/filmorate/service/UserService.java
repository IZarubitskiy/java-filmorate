package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final String msg = "Пользователь не найден";
    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public User addFriend(Long userId, long friendId){
        User user = userStorage.findById(userId).orElseThrow(()-> new NotFoundException(msg));
        User friend = userStorage.findById(userId).orElseThrow(()-> new NotFoundException(msg));

        if (user.getFriends().contains(friendId)){
            log.error("Добавление в друзья пользователя, являющегося другом.");
            throw new DuplicationException("Пользователь уже является другом.");
        }
        user.getFriends().add(friendId);
        log.debug("Добавление в список друзей инициатора с id = {} друга с id = {}.", userId, friendId);
        friend.getFriends().add(userId);
        log.debug("Добавление в список друзей пользователя с id = {} друга с id = {}.", friendId, userId);
        userStorage.updateFriends(user);
        log.debug("Обновление инициатора с is= {} в базе пользователей.", userId);
        userStorage.updateFriends(friend);
        log.debug("Обновление пользователя с is= {} в базе пользователей.", friendId);
        log.info("Пользователь с id = {} добавил в друзья пользователя с id = {}.", userId, friendId);
        return user;
    }

    public User removerFriend(Long userId, long notFriendId){
        User user = userStorage.findById(userId).orElseThrow(()-> new NotFoundException(msg));
        User friend = userStorage.findById(userId).orElseThrow(()-> new NotFoundException(msg));
        if (!user.getFriends().contains(notFriendId)){
            log.error("Удаление пользователя, не являющегося другом.");
            throw new DuplicationException("Пользователь еще не ваш друг.");
        }
        return user;

    }

    public Collection<User> getCommonFriends() {
        log.info("Получен список фильмов.");
        return films.values();
    }


}
