package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface FriendshipStorage {

    void add(Long userId, Long friendId);

    void remove(Long userId, Long friendId);

    void confirm (Long userId, Long friendId );

}