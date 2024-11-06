package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank
    @Email
    String email;
    String name;
    @NonNull
    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы")
    String login;
    @NonNull
    @NotBlank
    @Past
    LocalDate birthday;
}