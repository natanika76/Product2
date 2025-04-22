package ru.natali.medregistry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.natali.medregistry.dto.PatientDTO;
import ru.natali.medregistry.service.PatientService;


@RestController
@RequestMapping("/api/medical")
public class MedicalApiController {

    @Autowired
    private PatientService patientService;

    // Создание нового пациента
    /* POST http://localhost:8080/api/medical/patients
    {
        "firstName": "Майя",
        "lastName": "Тожеудалёнова",
        "dateOfBirth": "1990-01-01",
        "insuranceNumber": "INS357851"
     }
     */
    @PostMapping("/patients")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        return new ResponseEntity<>(patientService.createPatient(patientDTO), HttpStatus.CREATED);
    }

    // Получение пациента по номеру страховки
    //http://localhost:8080/api/medical/patients/insurance/INS123456
    // JSON {"id":1,"firstName":"Роман","lastName":"Брунов","dateOfBirth":"1985-05-15","insuranceNumber":"INS123456"}
    @GetMapping("/patients/insurance/{number}")
    public ResponseEntity<PatientDTO> getPatientByInsuranceNumber(@PathVariable String number) {
        return ResponseEntity.ok(patientService.getPatientByInsuranceNumber(number));
    }

    // Получение пациента по ID
    //GET http://localhost:8080/api/medical/patients/1
    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    // Обновление пациента не забывать, что нужна @Transactional в сервисе, иначе только html-страница с логином и паролем
    //PUT http://localhost:8080/api/medical/patients/3
    /*
    {
         "firstName": "Антон",
         "lastName": "Степанов",
         "dateOfBirth": "1988-08-08",
         "insuranceNumber": "INS987624"
     }
     */
    @PutMapping("/patients/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody PatientDTO updatedPatientDTO) {
        return ResponseEntity.ok(patientService.updatePatient(id, updatedPatientDTO));
    }

    // Логическое удаление пациента   //мягкое удаление: в таблице patient колонка deleted *true
    //DELETE  http://localhost:8080/api/medical/patients/2
    @DeleteMapping("/patients/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

}
