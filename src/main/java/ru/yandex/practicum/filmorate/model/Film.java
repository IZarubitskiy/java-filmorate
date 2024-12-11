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
    @NotBlank
    String name;
    @Size(max = 200, message = "Описание не может быть длиннее 200 символов")
    String description;
    LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительным числом")
    Integer duration;
    Long rate = 0L;
    Mpa mpa;
    Collection<Genre> genres = new ArrayList<>();

    public void increaseLikes() {
        rate++;
    }

    public void decreaseLikes() {
        if (rate > 0) {
            rate--;
        }
    }

}