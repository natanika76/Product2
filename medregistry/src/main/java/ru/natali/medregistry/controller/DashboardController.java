package ru.natali.medregistry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.natali.medregistry.model.Doctor;
import ru.natali.medregistry.model.Patient;
import ru.natali.medregistry.service.DoctorService;
import ru.natali.medregistry.service.PatientService;

import java.util.List;

@Controller
public class DashboardController {
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public DashboardController(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Получаем списки врачей и пациентов из сервисов
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<Patient> patients = patientService.getAllPatients();

        // Передаем данные в шаблон
        model.addAttribute("doctors", doctors);
        model.addAttribute("patients", patients);

        return "dashboard"; // имя шаблона без расширения .html
    }
}
