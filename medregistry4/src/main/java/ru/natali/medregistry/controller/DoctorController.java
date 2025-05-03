package ru.natali.medregistry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.natali.medregistry.dto.DoctorDTO;
import ru.natali.medregistry.service.DoctorService;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Контроллер для управления врачами.
 * Предоставляет REST API для выполнения операций CRUD с врачами.
 */
@RestController
@RequestMapping("/api/doctors")
@Tag(name = "Управление врачами", description = "API для управления врачами")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * Получает список всех врачей.
     *
     * @return ResponseEntity со списком DTO врачей и HTTP статусом 200
     */
    @Operation(summary = "Получить всех врачей", description = "Возвращает список всех врачей")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DoctorDTO.class)))
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = doctorService.getAllDoctors();
        List<DoctorDTO> dtos = doctors.stream()
                .map(doctor -> {
                    DoctorDTO dto = new DoctorDTO();
                    dto.setId(doctor.getId());
                    dto.setFirstName(doctor.getFirstName());
                    dto.setLastName(doctor.getLastName());
                    dto.setSpecialization(doctor.getSpecialization());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Создает нового врача.
     *
     * @param doctorDTO DTO врача для создания
     * @return ResponseEntity с созданным DTO врача и HTTP статусом 201
     */
    @Operation(summary = "Создать нового врача", description = "Создает нового врача")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class)))
    })
    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(
            @Parameter(description = "Doctor object to be created", required = true)
            @RequestBody DoctorDTO doctorDTO) {
        return new ResponseEntity<>(doctorService.createDoctor(doctorDTO), HttpStatus.CREATED);
    }

    /**
     * Удаляет врача по идентификатору (мягкое удаление).
     *
     * @param id идентификатор врача для удаления
     * @return ResponseEntity с HTTP статусом 204 (No Content)
     */
    @Operation(summary = "Удалить врача", description = "Мягкое удаление врача по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(
            @Parameter(description = "ID of doctor to be deleted", required = true)
            @PathVariable Long id) {
        System.out.println("Удаляем доктора с ID=" + id);
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Обновляет данные врача по идентификатору.
     *
     * @param id идентификатор врача
     * @param updatedDoctorDTO обновленные данные врача
     * @return ResponseEntity с обновленным DTO врача и HTTP статусом 200
     */
    @Operation(summary = "Обновить данные врача", description = "Обновляет существующего врача по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found")})
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorDTO updatedDoctorDTO) {
        // Пусть исключение выбрасывается дальше - его перехватит GlobalExceptionHandler
        DoctorDTO updatedDoctor = doctorService.updateDoctor(id, updatedDoctorDTO);
        return ResponseEntity.ok(updatedDoctor);
    }

    /**
     * Получает врача по идентификатору.
     *
     * @param id идентификатор врача
     * @return ResponseEntity с DTO врача и HTTP статусом 200
     * @throws EntityNotFoundException если врач не найден
     */
    @Operation(summary = "Получить врача по ID", description = "Возвращает врача по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved doctor",
                    content = @Content(schema = @Schema(implementation = DoctorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found")})
   //Изменила, чтобы исключение дошло до GlobalExceptionHandler для тестирования
    /* @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(
            @Parameter(description = "ID врача") @PathVariable Long id) {
        try {
            DoctorDTO doctorDto = doctorService.getDoctorById(id);
            return ResponseEntity.ok(doctorDto);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }*/
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        DoctorDTO doctorDto = doctorService.getDoctorById(id); // Исключение пробрасывается выше
        return ResponseEntity.ok(doctorDto);
    }
}