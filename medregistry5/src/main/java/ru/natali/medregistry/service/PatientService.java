package ru.natali.medregistry.service;

import ru.natali.medregistry.dto.PatientDTO;
import ru.natali.medregistry.model.Patient;

import java.util.List;

public interface PatientService {
    PatientDTO createPatient(PatientDTO patientDTO);
    PatientDTO updatePatient(Long id, PatientDTO patientDTO);
    void deletePatient(Long id);
    PatientDTO getPatientById(Long id);
    PatientDTO getPatientByInsuranceNumber(String insuranceNumber);
    PatientDTO convertToDto(Patient patient);
    Patient convertToEntity(PatientDTO patientDTO);
    List<Patient> getAllPatients(); // Возвращаем List<Doctor>
}
