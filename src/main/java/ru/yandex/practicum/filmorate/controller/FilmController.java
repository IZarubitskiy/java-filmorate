package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films")
    public Collection<Film> get() {
        return filmService.get();
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/films/{id}")
    public Film getById(@PathVariable("id") Long id) {
        return filmService.getById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikeToFilm(
            @PathVariable("id") Long id,
            @PathVariable("userId") Long userId) {
        filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFromFilm(
            @PathVariable("id") Long id,
            @PathVariable("userId") Long userId) {
        filmService.deleteLikeFromFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/")
    public void deleteFilmById(
            @PathVariable("id") Long id) {
        filmService.deleteFilmById(id);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getPopular(count);
    }
}
