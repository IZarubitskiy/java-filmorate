package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;

@Slf4j
@Data
public class Film {
    long id;
    @NonNull
    @NotBlank
    String name;
    @Size(min = 0, max = 200)
    String description;
    @NonNull
    LocalDate releaseDate;
    @NonNull
    @Positive
    Duration duration;
}
