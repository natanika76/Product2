package ru.natali.earthquake.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.natali.earthquake.model.Earthquake;
import ru.natali.earthquake.repository.EarthquakeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EarthquakeService {
    @Autowired
    private EarthquakeRepository earthquakeRepository;

    public List<Earthquake> findAll() {
        return earthquakeRepository.findAll();
    }

    public List<Earthquake> findByMagnitudeGreaterThan(double magnitude) {
        return earthquakeRepository.findByMagnitudeGreaterThan(magnitude);
    }

    public List<Earthquake> findByTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return earthquakeRepository.findByTimeBetween(startTime, endTime);
    }

    public void save(Earthquake earthquake) {
        earthquakeRepository.save(earthquake);
    }

    // Новый метод для сохранения списка объектов
    public void saveAll(List<Earthquake> earthquakes) {
        for (Earthquake earthquake : earthquakes) {
            earthquakeRepository.save(earthquake);
        }
    }
}
