package ru.natali.medregistry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.natali.medregistry.service.DoctorService;
import ru.natali.medregistry.service.PatientService;

// controller/WebController.java
@Controller
@RequestMapping("/")
public class WebController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

   /* @GetMapping
    public String home() {
        return "login";
    }*/

    @GetMapping("/doctors")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String doctorsPage(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "doctors";
    }

    @GetMapping("/patients")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String patientsPage(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "patients";
    }
}
