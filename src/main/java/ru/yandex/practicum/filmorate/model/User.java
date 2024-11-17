package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = { "email" })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    @NonNull
    @NotBlank
    @Email
    String email;
    String name;
    @NonNull
    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы")
    String login;
    @NonNull
    @Past
    LocalDate birthday;
    Set<Long> friends = new HashSet<>();
}