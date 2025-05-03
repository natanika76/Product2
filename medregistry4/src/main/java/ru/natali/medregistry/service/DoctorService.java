package ru.natali.medregistry.service;

import ru.natali.medregistry.dto.DoctorDTO;
import ru.natali.medregistry.model.Doctor;

import java.util.List;

public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO doctorDTO);
    DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO);
    void deleteDoctor(Long id);
    DoctorDTO getDoctorById(Long id);
    DoctorDTO convertToDto(Doctor doctor);
    Doctor convertToEntity(DoctorDTO doctorDTO);
    //List<Doctor> getAllDoctors(); // Возвращаем List<Doctor>
    List<DoctorDTO> getAllDoctors();
}
