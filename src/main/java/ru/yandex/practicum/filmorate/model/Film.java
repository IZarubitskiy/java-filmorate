package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"name"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
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
    Long rating = 0L;
    Mpa mpa;
    private Collection<Genre> genres = new ArrayList<>();

    public void increaseLikes() {
        rating++;
    }

    public void decreaseLikes() {
        if (rating > 0) {
            rating--;
        }
    }

}