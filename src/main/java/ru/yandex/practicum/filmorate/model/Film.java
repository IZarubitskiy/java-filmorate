package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = { "name" })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    Long id;
    @NonNull
    @NotBlank
    String name;
    @Size(max = 200, message = "Описание не может быть длиннее 200 символов")
    String description;
    @NonNull
    LocalDate releaseDate;
    @NonNull
    @Positive(message = "Продолжительность должна быть положительным числом")
    Integer duration;
}