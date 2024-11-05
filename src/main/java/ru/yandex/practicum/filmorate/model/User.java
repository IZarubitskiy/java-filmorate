package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Data
public class User {
    Long id;
    @NonNull
    @Email
    String email;
    @NonNull
    @NotBlank
    String login;
    String name;
    @NonNull
    @Past
    LocalDate birthday;
}
