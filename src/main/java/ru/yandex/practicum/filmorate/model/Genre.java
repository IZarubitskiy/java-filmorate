package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"name"})
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private int id;
    private String name;
}