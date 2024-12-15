package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final String msgUser = "Пользователь не найден";
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public Collection<User> get() {
        return userStorage.get();
    }

    public User add(User user) {

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Необходим email при добавлении.");
        }

        Optional<User> alreadyExistUser = userStorage.findByEmail(user.getEmail());
        if (alreadyExistUser.isPresent()) {
            throw new DuplicationException("Данный email уже используется.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Вместо имени использован логин при Добавлении");
        }

        return userStorage.add(user);
    }

    public User update(User user) {
        User user1 = userStorage.getUserById(user.getId())
                .orElseThrow(() -> new NotFoundException(msgUser));

        setUserName(user);

        return userStorage.update(user);
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException(msgUser));
    }

    public Collection<User> getFriends(Long id) {
        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException(msgUser));
        return userStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(Long user1Id, Long user2Id) {
        return userStorage.getCommonFriends(user1Id, user2Id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user1 = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(msgUser));
        User user2 = userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException(msgUser));
        friendshipStorage.add(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user1 = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(msgUser));
        User user2 = userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException(msgUser));
        friendshipStorage.remove(userId, getUserById(friendId).getId());
    }

    public void confirmFriend(Long userId, Long friendId) {
        friendshipStorage.confirm(userId, friendId);
    }

    private void setUserName(User user) {
        if (!Objects.nonNull(user.getName()) && !user.getName().isEmpty() && !user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
