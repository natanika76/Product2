package ru.natali.earthquake.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ru.natali.earthquake.model.Earthquake;
import ru.natali.earthquake.service.EarthquakeService;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class EarthquakeDataLoader {
    @Autowired
    private EarthquakeService earthquakeService;

    @PostConstruct
    public void loadInitialData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URL url = new URL("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson");
        byte[] jsonData = url.openStream().readAllBytes();
        String data = new String(jsonData, StandardCharsets.UTF_8);
        List<Earthquake> earthquakes = parseGeoJson(data);
        earthquakeService.saveAll(earthquakes);
    }

    private List<Earthquake> parseGeoJson(String geoJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); // Создаем экземпляр ObjectMapper
        // Читаем корневой объект GeoJSON
        JsonNode root = mapper.readTree(geoJson);
        // Получаем массив "features", который содержит отдельные объекты землетрясений
        JsonNode features = root.get("features");
        if (features == null || !features.isArray()) {
            throw new IllegalArgumentException("Invalid GeoJSON format: missing or invalid 'features' array.");
        }
        // Создаем пустой список для хранения объектов Earthquake
        List<Earthquake> earthquakes = new ArrayList<>();
        // Проходимся по каждому элементу массива "features"
        for (JsonNode feature : features) {
            // Проверяем наличие обязательных свойств
            JsonNode properties = feature.get("properties");
            if (properties == null) {
                continue; // Пропускаем элемент, если свойства отсутствуют
            }
            JsonNode geometry = feature.get("geometry");
            if (geometry == null) {
                continue; // Пропускаем элемент, если геометрия отсутствует
            }
            // Извлекаем необходимые поля
            String title = properties.get("title").asText();
            double magnitude = properties.get("mag").asDouble();
            long timeInMillis = properties.get("time").asLong();
            LocalDateTime time = Instant.ofEpochMilli(timeInMillis).atZone(ZoneOffset.UTC).toLocalDateTime();
            String place = geometry.get("coordinates").get(0).asText(); // Место (координаты)

            // Создаем объект Earthquake и добавляем его в список
            Earthquake earthquake = new Earthquake(title, time, magnitude, place);
            earthquakes.add(earthquake);
        }
        return earthquakes;
    }
}