package ru.natali.earthquake.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natali.earthquake.model.Earthquake;

import java.time.LocalDateTime;
import java.util.List;

public interface EarthquakeRepository extends JpaRepository<Earthquake, Long> {
    // Метод для поиска землетрясений с магнитудой больше указанной
    List<Earthquake> findByMagnitudeGreaterThan(Double magnitude);

    // Метод для поиска землетрясений между двумя временными метками
    List<Earthquake> findByTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
