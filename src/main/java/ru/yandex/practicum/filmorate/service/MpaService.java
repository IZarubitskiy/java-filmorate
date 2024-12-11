package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor

public class MpaService {

    private final MpaStorage mpaStorage;

    public Mpa getMpaById(Long id) {
        Mpa mpa = mpaStorage.getMpaById(id);
        checkMpaIsNotNull(mpa, id);
        log.info("Получен MPA по Id: {}", id);
        return mpa;
    }

    public Collection<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    private void checkMpaIsNotNull(Mpa mpa, Long id) {
        if (Objects.isNull(mpa) || Objects.isNull(getAllMpa().stream()
                .collect(Collectors.toMap(Mpa::getId, m -> m)).get(mpa.getId()))) {
            throw new NotFoundException(String.format("MPA рейтинга с id %s нет", id));
        }
    }
}