package ru.natali.medregistry.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natali.medregistry.dto.PatientDTO;
import ru.natali.medregistry.model.Doctor;
import ru.natali.medregistry.model.Patient;
import ru.natali.medregistry.repository.PatientRepository;
import ru.natali.medregistry.service.PatientService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        // Проверка через existsByInsuranceNumber
        if (patientDTO.getInsuranceNumber() != null &&
                patientRepository.existsByInsuranceNumber(patientDTO.getInsuranceNumber())) {
            throw new IllegalArgumentException("Patient with this insurance number already exists");
        }

        Patient patient = convertToEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patient);
        return convertToDto(savedPatient);
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));

        if (!existingPatient.getInsuranceNumber().equals(patientDTO.getInsuranceNumber())) {
            if (patientRepository.existsByInsuranceNumber(patientDTO.getInsuranceNumber())) {
                throw new IllegalArgumentException("Patient with this insurance number already exists");
            }
        }

        modelMapper.map(patientDTO, existingPatient);
        Patient updatedPatient = patientRepository.save(existingPatient);
        return convertToDto(updatedPatient);
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
        patientRepository.softDelete(id);
    }

    @Override
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
        return convertToDto(patient);
    }

    @Override
    public PatientDTO getPatientByInsuranceNumber(String insuranceNumber) {
        Patient patient = patientRepository.findByInsuranceNumber(insuranceNumber)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with insurance number: " + insuranceNumber));
        return convertToDto(patient);
    }

   /* @Override
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAllActive().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }*/

    @Override
    public PatientDTO convertToDto(Patient patient) {
        return modelMapper.map(patient, PatientDTO.class);
    }

    @Override
    public Patient convertToEntity(PatientDTO patientDTO) {
        return modelMapper.map(patientDTO, Patient.class);
    }
    @Override
    public List<Patient> getAllPatients() { // Точно такой же возвращаемый тип
        return patientRepository.findAll();
    }
}