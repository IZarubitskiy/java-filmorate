package ru.yandex.practicum.filmorate.storage.friendship;

public interface FriendshipStorage {

    void add(Long userId, Long friendId);

    void remove(Long userId, Long friendId);

    void confirm(Long userId, Long friendId);

}