package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private static final String FILMS_SQL =
            "select f.*," +
                    " m.id as mpa_id," +
                    " m.name as mpa_name " +
                    "from films f " +
                    "left join film_mpas fm on f.id = fm.film_id " +
                    "left join mpas m on fm.mpa_id = m.id";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {


        final String sql = "insert into films (name, release_date, description, duration, rate) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setObject(2, film.getReleaseDate());
            preparedStatement.setString(3, film.getDescription());
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setLong(5, film.getRate());

            return preparedStatement;
        }, generatedKeyHolder);

        Long filmId = Objects.requireNonNull(generatedKeyHolder.getKey()).longValue();

        film.setId(filmId);

        return film;
    }


    @Override
    public Optional<Film> getFilmById(Long filmId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FILMS_SQL.concat(" where f.id = ?"), new FilmMapper(), filmId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Film> get() {
        return jdbcTemplate.query(FILMS_SQL, new FilmMapper());
    }

    @Override
    public Film update(Film film) {
        final String sql = "update films set name = ?, release_date = ?, description = ?, duration = ?, " +
                "rate = ? where id = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getRate(),
                film.getId()
        );

        return film;
    }

    @Override
    public Collection<Film> getPopular(Long count, Long genreId, Long year) {
        final Collection<String> params = new ArrayList<>();
        String sql =
                "select f.*, m.id as mpa_id, m.name as mpa_name from films f left join likes l on f.id = l.film_id " +
                        "left join film_mpas fm on f.id = fm.film_id left join mpas m on fm.mpa_id = m.id " +
                        "left join film_genres fg on f.id = fg.film_id %s group by f.name, f.id order by count(l.film_id) desc limit ?";

        if (Objects.nonNull(genreId)) {
            params.add(String.format("genre_id = %s", genreId));
        }

        if (Objects.nonNull(year)) {
            params.add(String.format("YEAR(release_date) = %s", year));
        }

        final String genreAndYearParams = !params.isEmpty() ? "where ".concat(String.join(" and ", params)) : "";

        return jdbcTemplate.query(String.format(sql, genreAndYearParams), new FilmMapper(), count);
    }

    public boolean deleteFilmById(Long id) {
        final String sql = "delete from films where id = ?";
        int status = jdbcTemplate.update(sql, id);
        return status != 0;
    }

}