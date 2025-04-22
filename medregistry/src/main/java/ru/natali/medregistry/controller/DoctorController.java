package ru.natali.medregistry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.natali.medregistry.dto.DoctorDTO;
import ru.natali.medregistry.model.Doctor;
import ru.natali.medregistry.service.DoctorService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    //Получение всех врачей
    // GET http://localhost:8080/api/doctors
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<DoctorDTO> dtos = doctors.stream()
                .map(doctor -> {
                    DoctorDTO dto = new DoctorDTO();
                    dto.setId(doctor.getId());                  // Заполняем id врача
                    dto.setFirstName(doctor.getFirstName());    // Имя врача
                    dto.setLastName(doctor.getLastName());      // Фамилия врача
                    dto.setSpecialization(doctor.getSpecialization()); // Специализация врача
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);                         // Возвращаем заполненный список врачей
    }

    //Создание нового доктора
    /* POST http://localhost:8080/api/doctors
    {
      "firstName": "Глеб",
      "lastName": "Петров",
      "specialization": "вирусолог"
    }
    {
      "firstName": "Иван",
      "lastName": "Дляудалёнов",
      "specialization": "физиолог"
    } */
    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        return new ResponseEntity<>(doctorService.createDoctor(doctorDTO), HttpStatus.CREATED);
    }

    //Мягкое удаление: в таблице doctor колонка deleted *true
    //DELETE  http://localhost:8080/api/doctors/5   //
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        System.out.println("Удаляем доктора с ID=" + id); // Логи
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    //Получение докотора по ID
    // GET http://localhost:8080/api/doctors/3
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        DoctorDTO doctorDto = doctorService.getDoctorById(id);
        if (doctorDto != null) {
            return ResponseEntity.ok(doctorDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Изменение доктора по ID не забывать, что нужна @Transactional в сервисе, иначе только html-страница с логином и паролем
   /* //PUT http://localhost:8080/api/doctors/3
   {
    "firstName": "Потап",
    "lastName": "Иванов",
    "specialization": "лор"
} */
    @PutMapping("/{id}")
//@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO updatedDoctorDTO) {
        DoctorDTO updatedDoctor = doctorService.updateDoctor(id, updatedDoctorDTO);
        if (updatedDoctor != null) {
            return ResponseEntity.ok(updatedDoctor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

