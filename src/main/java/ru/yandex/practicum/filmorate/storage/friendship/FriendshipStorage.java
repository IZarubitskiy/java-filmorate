package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface FriendshipStorage {

    Collection<User> get(Long userId);

    Collection<User> getCommon(Long userId, long otherId);

    boolean add(Integer userId, Integer friendId);

    void remove(Integer userId, Integer friendId);

}