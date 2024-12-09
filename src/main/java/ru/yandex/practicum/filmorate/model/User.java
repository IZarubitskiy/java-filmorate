package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"email"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
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
}