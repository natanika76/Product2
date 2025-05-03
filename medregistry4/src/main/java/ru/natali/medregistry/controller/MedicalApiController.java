package ru.natali.medregistry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.natali.medregistry.dto.PatientDTO;
import ru.natali.medregistry.service.PatientService;

/**
 * Контроллер для управления пациентами (API регистратуры).
 * Предоставляет REST API для работы с пациентами медицинского учреждения.
 */
@RestController
@RequestMapping("/api/medical")
@Tag(name = "API регистратуры", description = "API для управления пациентами")
public class MedicalApiController {

    @Autowired
    private PatientService patientService;

    @Operation(
            summary = "Создание нового пациента",
            description = "Позволяет создать нового пациента в системе"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пациент успешно создан",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    @PostMapping("/patients")
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        return new ResponseEntity<>(patientService.createPatient(patientDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Получение пациента по номеру страховки",
            description = "Возвращает информацию о пациенте по его номеру страхового полиса"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение данных пациента",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пациент не найден")
    })
    @GetMapping("/patients/insurance/{number}")
    public ResponseEntity<PatientDTO> getPatientByInsuranceNumber(
            @Parameter(description = "Номер страхового полиса", required = true)
            @PathVariable String number) {
        return ResponseEntity.ok(patientService.getPatientByInsuranceNumber(number));
    }

    @Operation(
            summary = "Получение пациента по ID",
            description = "Возвращает информацию о пациенте по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение данных пациента",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пациент не найден")
    })
    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDTO> getPatientById(
            @Parameter(description = "ID пациента", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @Operation(
            summary = "Обновление данных пациента",
            description = "Позволяет обновить информацию о существующем пациенте"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные пациента успешно обновлены",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Пациент не найден")
    })
    @PutMapping("/patients/{id}")
    public ResponseEntity<PatientDTO> updatePatient(
            @Parameter(description = "ID пациента", required = true)
            @PathVariable Long id,
            @RequestBody PatientDTO updatedPatientDTO) {
        return ResponseEntity.ok(patientService.updatePatient(id, updatedPatientDTO));
    }

    @Operation(
            summary = "Удаление пациента",
            description = "Выполняет логическое удаление пациента (мягкое удаление)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пациент успешно удален"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Пациент не найден")
    })
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "ID пациента", required = true)
            @PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}