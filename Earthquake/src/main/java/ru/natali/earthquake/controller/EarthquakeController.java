package ru.natali.earthquake.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.natali.earthquake.model.Earthquake;
import ru.natali.earthquake.service.EarthquakeService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class EarthquakeController {

    @Autowired
    private EarthquakeService earthquakeService;

    //http://localhost:8080/earthquakes
    @GetMapping("/earthquakes")
    public String getAllEarthquakes(Model model) {
        List<Earthquake> earthquakes = earthquakeService.findAll();
        model.addAttribute("earthquakes", earthquakes);
        return "earthquakes"; // Общий отчёт
    }

    //http://localhost:8080/earthquakes/magnitude-greater-than?magnitude=2.5
    @GetMapping("/earthquakes/magnitude-greater-than")
    public String getEarthquakesWithMagnitudeGreaterThan(Model model, @RequestParam("magnitude") double magnitude) {
        try {
            List<Earthquake> earthquakes = earthquakeService.findByMagnitudeGreaterThan(magnitude);
            model.addAttribute("earthquakes", earthquakes);
            return "magnitude-filtered";
        } catch (Exception e) {
            // Логирование или обработка ошибки
            System.out.println("Error occurred: " + e.getMessage());
            return "error"; // Переход на страницу с ошибкой
        }
    }
    //нужен именно такой запрос, экранировать знаки препинания, иначе не работает
//http://localhost:8080/earthquakes/time-between?startTime=2025-04-11T17%3A43%3A56.764&endTime=2025-04-11T18%3A20%3A21.610
    @GetMapping("/earthquakes/time-between")
    public String getEarthquakesBetweenTimes(Model model,
                                             @RequestParam("startTime") LocalDateTime startTime,
                                             @RequestParam("endTime") LocalDateTime endTime) {
        try {
            List<Earthquake> earthquakes = earthquakeService.findByTimeBetween(startTime, endTime);
            model.addAttribute("earthquakes", earthquakes);
            return "time-filtered";
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return "error"; // Переход на страницу с ошибкой
        }
    }
}