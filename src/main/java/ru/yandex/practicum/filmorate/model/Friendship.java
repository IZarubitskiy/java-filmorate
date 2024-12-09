package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor

public class Friendship {
    private int id;
    private boolean confirmation;
}
