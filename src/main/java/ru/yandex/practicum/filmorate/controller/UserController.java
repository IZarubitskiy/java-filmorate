package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public Collection<User> findAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/users/{id}")
    public Optional<User> findByID(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable("id") Long id,
            @PathVariable("friendId") Long friendId) {
        userService.addFriend(id, friendId);
    }

    @PutMapping("/users/confirmation/{id}/friends/{friendId}")
    public void confirmFriend(
            @PathVariable("id") Long id,
            @PathVariable("friendId") Long friendId) {
        userService.confirmFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removerFriend(
            @PathVariable("id") Long id,
            @PathVariable("friendId") Long friendId) {
         userService.removeFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable("id") Long id,
            @PathVariable("otherId") Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
