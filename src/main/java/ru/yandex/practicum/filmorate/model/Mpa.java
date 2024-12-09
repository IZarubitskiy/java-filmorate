package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"name"})
@NoArgsConstructor
@AllArgsConstructor
public class Mpa {
    private int id;
    private String name;
}