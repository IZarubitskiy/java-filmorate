package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"name"})
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
    Long rate = 0L;
    Set<String> genre = new HashSet<>();
    String rating;

    public void increaseLikes() {
        rate++;
    }

    public void decreaseLikes() {
        if (rate > 0) {
            rate--;
        }
    }

}