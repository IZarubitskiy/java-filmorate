package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"email"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    @NonNull
    @NotNull
    @NotBlank
    @Email
    String email;
    String name;
    @NonNull
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[^\\s]+$", message = "Логин не должен содержать пробелы")
    String login;
    @NonNull
    @NotNull
    @Past
    LocalDate birthday;
}