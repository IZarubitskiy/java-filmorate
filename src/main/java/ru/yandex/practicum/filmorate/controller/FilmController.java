package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("{id}")
    public Optional<Film> findByID(@PathVariable("id") Long id) {
        return filmService.findById(id);
    }

    @PutMapping("{id}/like/{userId}")
    public Film like(
            @PathVariable("id") Long id,
            @PathVariable("userId") Long userId) {
        return filmService.like(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film unlike(
            @PathVariable("id") Long id,
            @PathVariable("userId") Long userId) {
        return filmService.unlike(id, userId);
    }

    @GetMapping("popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }
}
