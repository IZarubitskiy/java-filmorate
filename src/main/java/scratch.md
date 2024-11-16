/*
public void customFilmValidator(Film film) throws ValidationException {
if (film.getName().isBlank() ||
film.getName() == null) {
throw new ValidationException("Фильм без названия");
}
if (film.getDescription() != null) {
if (film.getDescription().length() > 200) {
throw new ValidationException("Описание содержит более 200 символов.");
}
}
if (film.getDuration() < 0) {
throw new ValidationException("Продолжительность фильма отрицательная.");
}
if (film.getReleaseDate().isBefore(LocalDate.parse("28.12.1895", DateTimeFormatter.ofPattern("dd.MM.yyyy")))) {
throw new ValidationException("Выбрана дата до 28 декабря 1895 года.");
}
}
*/



    public void customUserValidator(User user) throws ValidationException {
        if (user.getEmail().isBlank() ||
                user.getEmail() == null) {
            throw new ValidationException("Пустой email");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email не содержит @.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getLogin() == null
                || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }
    }