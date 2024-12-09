package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> get();

    User add(User user);

    User update(User user);

    User getById(Long id);

    Collection<User> getFriends(Integer userId);

    Collection<User> getCommonFriends(Integer user1Id, Integer user2Id);

    boolean deleteUserById(Integer id);
}
