package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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
        User friend = userStorage.findById(friendId).orElseThrow(() -> new NotFoundException(msg));

        if (user.getFriends().contains(friendId)){
            log.error("Добавление в друзья пользователя, являющегося другом.");
            throw new DuplicationException("Пользователь уже является другом.");
        }
        user.getFriends().add(friendId);
        log.debug("Добавление в список друзей инициатора с id = {} друга с id = {}.", userId, friendId);
        friend.getFriends().add(userId);
        log.debug("Добавление в список друзей пользователя с id = {} друга с id = {}.", friendId, userId);
        userStorage.updateFriends(user);
        log.debug("Обновление инициатора с is= {} в базе пользователей, после добавления друга.", userId);
        userStorage.updateFriends(friend);
        log.debug("Обновление пользователя с is= {} в базе пользователей, после добавления друга.", friendId);
        log.info("Пользователь с id = {} добавил в друзья пользователя с id = {}.", userId, friendId);
        return user;
    }

    public User removerFriend(Long userId, long notFriendId){
        User user = userStorage.findById(userId).orElseThrow(()-> new NotFoundException(msg));
        User friend = userStorage.findById(notFriendId).orElseThrow(() -> new NotFoundException(msg));
        if (!user.getFriends().contains(notFriendId)){
            log.error("Удаление пользователя, не являющегося другом.");
            throw new DuplicationException("Пользователь еще не ваш друг.");
        }
        user.getFriends().remove(notFriendId);
        log.debug("Удаление из списка друзей инициатора с id = {} друга с id = {}.", userId, notFriendId);
        friend.getFriends().remove(userId);
        log.debug("Удаление из списка друзей пользователя с id = {} друга с id = {}.", notFriendId, userId);
        userStorage.updateFriends(user);
        log.debug("Обновление инициатора с is= {} в базе пользователей, после удаления друга.", userId);
        userStorage.updateFriends(friend);
        log.debug("Обновление пользователя с is= {} в базе пользователей, после удаления друга.", notFriendId);
        log.info("Пользователь с id = {} удалил из друзей пользователя с id = {}.", userId, notFriendId);

        return user;
    }

    public Collection<User> getFriends(Long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException(msg));
        log.info("Получение списка друзей пользователя с id = {}", user.getId());
        return user.getFriends().stream()
                .map(friendId -> userStorage.findById(friendId)
                        .orElseThrow(() -> new NotFoundException(msg)))
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(Long userId, long friendId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException(msg));
        User userFriend = userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException(msg));
        Set<Long> currentUserFriends = user.getFriends();
        Set<Long> friendFriends = userFriend.getFriends();
        log.info("Получен список общих друзей пользователя с id = {} и пользователя с id = {}",
                user.getId(),
                userFriend.getId());
        return currentUserFriends.stream()
                .filter(friendFriends::contains)
                .map(id -> userStorage.findById(id)
                        .orElseThrow(() -> new NotFoundException(msg)))
                .collect(Collectors.toList());
    }
}
