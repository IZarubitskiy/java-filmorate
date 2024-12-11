package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final String MPAS_SQL = "select * from mpas";

    @Override
    public Mpa getMpaById(Long mpaId) {
        try {
            return jdbcTemplate.queryForObject(MPAS_SQL.concat(" where id = ?"), new MpaMapper(), mpaId);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        log.info("Получен список MPA.");
        return jdbcTemplate.query(MPAS_SQL, new MpaMapper());
    }

    @Override

    public boolean containsMpa(Long mpaId){
        Integer count = jdbcTemplate.queryForObject(
                MPAS_SQL.concat(" where id = ?"),
                Integer.class,
                mpaId
        );

        if (count == null || count == 0) {
            return true;
        }
        return false;
    }
}