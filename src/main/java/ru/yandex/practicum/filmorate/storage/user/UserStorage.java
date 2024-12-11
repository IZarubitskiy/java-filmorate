package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> get();

    User add(User user);

    User update(User user);

    Optional<User> getUserById(Long id);

    Collection<User> getFriends(Long id);

    Collection<User> getCommonFriends(Long user1Id, Long user2Id);

    boolean deleteUserById(Long id);

    boolean contains(Long id );

    Optional<User> findByEmail(String email);
}
