package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final String msg = "Пользователь не найден";
    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.get();
    }

    public User create(User user) {
        return userStorage.add(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public Optional<User> findById(Long id) {
        return userStorage.getById(id);
    }

}
