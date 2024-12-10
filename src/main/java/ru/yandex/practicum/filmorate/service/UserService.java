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
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final String NOT_FOUND_USER = "Пользователь не найден";
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public Collection<User> findAll() {
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

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Необходим email при обновлении.");
        }
        if (user.getId() == null) {
            log.error("Id не указан");
            throw new ValidationException("Id должен быть указан");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Вместо имени использован логин при обновлении");
        }

        return userStorage.update(user);
    }

    public Optional<User> getById(Long id) {
        return userStorage.getById(id);
    }

    public Collection<User> getFriends (Long id) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        return userStorage.getFriends(id);}

    public Collection<User> getCommonFriends(Long user1Id, Long user2Id){
        User user1 = userStorage.getById(user1Id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        User user2 = userStorage.getById(user2Id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        return userStorage.getCommonFriends(user1Id, user2Id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user1 = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        User user2 = userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        friendshipStorage.add(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user1 = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        User user2 = userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        friendshipStorage.remove(userId, friendId );
    }

    public void confirmFriend (Long userId, Long friendId ){
        User user1 = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        User user2 = userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        friendshipStorage.confirm(userId, friendId);
    }

}
