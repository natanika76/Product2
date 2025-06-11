package ru.natali.medregistry.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natali.medregistry.dto.PatientDTO;
import ru.natali.medregistry.model.Patient;
import ru.natali.medregistry.repository.PatientRepository;
import ru.natali.medregistry.service.PatientService;

import java.util.List;
/**
 * Реализация сервиса для работы с пациентами.
 * Обеспечивает бизнес-логику управления пациентами, включая кэширование.
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "patients")
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    @CacheEvict(allEntries = true)
    public PatientDTO createPatient(PatientDTO patientDTO) {
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
    @CacheEvict(key = "#id")
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
        patientRepository.softDelete(id);
    }

    @Override
    @Cacheable(key = "#id")
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
        return convertToDto(patient);
    }

    @Override
    @Cacheable(key = "#insuranceNumber")
    public PatientDTO getPatientByInsuranceNumber(String insuranceNumber) {
        Patient patient = patientRepository.findByInsuranceNumber(insuranceNumber)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with insurance number: " + insuranceNumber));
        return convertToDto(patient);
    }

    @Override
    @Cacheable
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
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

}