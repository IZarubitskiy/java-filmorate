package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.filmMpa.FilmMpaStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.PreparedStatement;
import java.util.*;

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
    private final FilmMpaStorage filmMpaStorage;
    private final MpaStorage mpaStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final GenreDbStorage genreStorage;

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

        return addExtraFields(film);
    }


    @Override
    public Film getFilmById(Long filmId) {
        List<Film> films = jdbcTemplate.query(FILMS_SQL.concat(" where f.id = ?"), new FilmMapper(), filmId);

        if (!films.isEmpty()) {
            Collection<Genre> filmGenres = filmGenreStorage.getAllFilmGenresById(filmId);

            return films.get(0).toBuilder().genres(filmGenres).build();
        }
        return null;
    }

    @Override
    public Film findByName(String name) {
        List<Film> films = jdbcTemplate.query(FILMS_SQL.concat(" where f.name = ?"), new FilmMapper(), name);

        if (!films.isEmpty()) {
            Collection<Genre> filmGenres = filmGenreStorage.getAllFilmGenresById(films.getFirst().getId());

            return films.getFirst().toBuilder().genres(filmGenres).build();
        }

        return null;
    }

    @Override
    public Collection<Film> get() {
        Collection<Film> films = jdbcTemplate.query(FILMS_SQL, new FilmMapper());

        return setFilmGenres(films);
    }

    @Override
    public Film update(Film film) {
        final String sql = "update films set name = ?, release_date = ?, description = ?, duration = ?, " +
                "rate = ? where id = ?";

        filmMpaStorage.deleteFilmMpaById(film.getId());
        filmGenreStorage.deleteAllFilmGenresById(film.getId());

        jdbcTemplate.update(sql,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getRate(),
                film.getId()
        );

        return addExtraFields(film);
    }

    @Override
    public Collection<Film> getPopular(Long count) {
        final Collection<String> params = new ArrayList<>();
        String sql =
                "select f.*, m.id as mpa_id, m.name as mpa_name from films f left join likes l on f.id = l.film_id " +
                        "left join film_mpas fm on f.id = fm.film_id left join mpas m on fm.mpa_id = m.id " +
                        "left join film_genres fg on f.id = fg.film_id %s group by f.name, f.id order by count(l.film_id) desc limit ?";

        Collection<Film> films = jdbcTemplate.query(sql, new FilmMapper(), count);

        return setFilmGenres(films);
    }
    private Collection<Film> setFilmGenres(Collection<Film> films) {
        Map<Long, Collection<Genre>> filmGenresMap = filmGenreStorage.getAllFilmGenres(films);

        films.forEach(film -> {
            Long filmId = film.getId();

            film.setGenres(filmGenresMap.getOrDefault(filmId, new ArrayList<>()));
        });

        return films;
    }

    public boolean deleteFilmById(Long id) {
        final String sql = "delete from films where id = ?";
        int status = jdbcTemplate.update(sql, id);
        return status != 0;
    }

    private Film setFilmGenresAndMpa(Film film) {
        Collection<Genre> filmGenres = filmGenreStorage.getAllFilmGenresById(film.getId());
        film.setGenres(filmGenres);
        Mpa filmMpa = filmMpaStorage.getFilmMpaById(film.getId());
        film.setMpa(filmMpa);

        return film;
    }

    private Film addExtraFields(Film film) {

        Long filmId = film.getId();
        Long mpaId = film.getMpa().getId();

        filmMpaStorage.addFilmMpa(filmId, mpaId);
        film.getGenres().forEach(genre -> filmGenreStorage.addFilmGenre(filmId, genre.getId()));

        Mpa filmMpa = mpaStorage.getMpaById(mpaId);
        Collection<Genre> filmGenres = filmGenreStorage.getAllFilmGenresById(filmId);

        return film.toBuilder().mpa(filmMpa).genres(filmGenres).build();
    }
}