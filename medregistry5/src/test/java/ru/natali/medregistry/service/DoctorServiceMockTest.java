package ru.natali.medregistry.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.natali.medregistry.dto.DoctorDTO;
import ru.natali.medregistry.exceptions.DoctorNotFoundException;
import ru.natali.medregistry.model.Doctor;
import ru.natali.medregistry.repository.DoctorRepository;
import ru.natali.medregistry.service.impl.DoctorServiceImpl;

import java.util.List;
import java.util.Optional;

/*
Вместо аннотации @SpringBootTest мы используем @ExtendWith(MockitoExtension.class),
 чтобы задействовать мощь Mockito для моков
 */
@ExtendWith(MockitoExtension.class)
public class DoctorServiceMockTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ModelMapper modelMapper; // Добавляем мок для ModelMapper

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor createTestDoctor(Long id, String firstName, String lastName, String specialization) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setSpecialization(specialization);
        return doctor;
    }

    private DoctorDTO createTestDoctorDTO(Long id, String firstName, String lastName, String specialization) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(id);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setSpecialization(specialization);
        return dto;
    }

    @Test
    public void getDoctorById_ShouldReturnDoctor() {
        // Given
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Иван");
        doctor.setLastName("Потапов");
        doctor.setSpecialization("кардиолог");

        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setFirstName("Иван");
        doctorDTO.setLastName("Потапов");
        doctorDTO.setSpecialization("кардиолог");

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(modelMapper.map(doctor, DoctorDTO.class)).thenReturn(doctorDTO); // Настраиваем поведение modelMapper

        // When
        DoctorDTO result = doctorService.getDoctorById(1L);

        // Then
        assertThat(result.getId(), is(1L));
        assertThat(result.getFirstName(), is("Иван"));
        assertThat(result.getLastName(), is("Потапов"));
        assertThat(result.getSpecialization(), is("кардиолог"));

        verify(doctorRepository).findById(1L);
        verify(modelMapper).map(doctor, DoctorDTO.class);
    }

    @Test
    public void getAllDoctors_ShouldReturnAllDoctors() {
        // Given
        Doctor doctor1 = createTestDoctor(1L, "Иван", "Потапов", "кардиолог");
        Doctor doctor2 = createTestDoctor(2L, "Эмилия", "Жданова", "нейрохирург");

        DoctorDTO doctorDTO1 = createTestDoctorDTO(1L, "Иван", "Потапов", "кардиолог");
        DoctorDTO doctorDTO2 = createTestDoctorDTO(2L, "Эмилия", "Жданова", "нейрохирург");

        // Используем findAll() вместо findAllActive()
        when(doctorRepository.findAll()).thenReturn(List.of(doctor1, doctor2));
        when(modelMapper.map(doctor1, DoctorDTO.class)).thenReturn(doctorDTO1);
        when(modelMapper.map(doctor2, DoctorDTO.class)).thenReturn(doctorDTO2);

        // When
        List<DoctorDTO> result = doctorService.getAllDoctors();

        // Then
        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder(doctorDTO1, doctorDTO2));
        verify(doctorRepository).findAll();  // Проверяем вызов findAll()
    }

    @Test
    public void getDoctorById_WithValidId_ShouldReturnDoctor() {
        // Given
        Doctor doctor = createTestDoctor(1L, "Иван", "Потапов", "кардиолог");
        DoctorDTO doctorDTO = createTestDoctorDTO(1L, "Иван", "Потапов", "кардиолог");

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(modelMapper.map(doctor, DoctorDTO.class)).thenReturn(doctorDTO);

        // When
        DoctorDTO result = doctorService.getDoctorById(1L);

        // Then
        assertThat(result, samePropertyValuesAs(doctorDTO));
        verify(doctorRepository).findById(1L);
    }

    @Test
    public void getDoctorById_WithInvalidId_ShouldThrowException() {
        // Given
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> doctorService.getDoctorById(99L));
        verify(doctorRepository).findById(99L);
    }

    @Test
    public void createDoctor_ShouldSaveAndReturnNewDoctor() {
        // Given
        DoctorDTO newDoctorDTO = createTestDoctorDTO(null, "Новый", "Врач", "терапевт");
        Doctor doctorToSave = createTestDoctor(null, "Новый", "Врач", "терапевт");
        Doctor savedDoctor = createTestDoctor(3L, "Новый", "Врач", "терапевт");
        DoctorDTO expectedDTO = createTestDoctorDTO(3L, "Новый", "Врач", "терапевт");

        when(modelMapper.map(newDoctorDTO, Doctor.class)).thenReturn(doctorToSave);
        when(doctorRepository.save(doctorToSave)).thenReturn(savedDoctor);
        when(modelMapper.map(savedDoctor, DoctorDTO.class)).thenReturn(expectedDTO);

        // When
        DoctorDTO result = doctorService.createDoctor(newDoctorDTO);

        // Then
        assertThat(result.getId(), is(3L));
        assertThat(result.getFirstName(), is("Новый"));
        verify(doctorRepository).save(doctorToSave);
    }

    /* Тестирование метода обновления доктора */
    @Test
    public void updateDoctor_WithValidId_ShouldUpdateDoctor() {
        // Given
        Doctor existingDoctor = createTestDoctor(1L, "Старое", "Имя", "специализация");
        DoctorDTO updateDTO = createTestDoctorDTO(1L, "Новое", "Имя", "новая специализация");
        Doctor updatedDoctor = createTestDoctor(1L, "Новое", "Имя", "новая специализация");
        DoctorDTO expectedDTO = createTestDoctorDTO(1L, "Новое", "Имя", "новая специализация");

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(existingDoctor));

        // Настраиваем два разных вызова modelMapper:
        // 1. Для обновления полей существующего доктора
        doAnswer(invocation -> {
            DoctorDTO source = invocation.getArgument(0);
            Doctor destination = invocation.getArgument(1);
            destination.setFirstName(source.getFirstName());
            destination.setLastName(source.getLastName());
            destination.setSpecialization(source.getSpecialization());
            return null;
        }).when(modelMapper).map(updateDTO, existingDoctor);

        // 2. Для преобразования обратно в DTO
        when(modelMapper.map(updatedDoctor, DoctorDTO.class)).thenReturn(expectedDTO);

        when(doctorRepository.save(existingDoctor)).thenReturn(updatedDoctor);

        // When
        DoctorDTO result = doctorService.updateDoctor(1L, updateDTO);

        // Then
        assertThat(result.getFirstName(), is("Новое"));
        assertThat(result.getSpecialization(), is("новая специализация"));

        verify(doctorRepository).findById(1L);
        verify(modelMapper).map(updateDTO, existingDoctor);
        verify(modelMapper).map(updatedDoctor, DoctorDTO.class);
        verify(doctorRepository).save(existingDoctor);
    }

    @Test
    public void updateDoctor_WithInvalidId_ShouldThrowException() {
        // Given
        DoctorDTO updateDTO = createTestDoctorDTO(99L, "Новое", "Имя", "специализация");
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DoctorNotFoundException.class, () -> doctorService.updateDoctor(99L, updateDTO));
        verify(doctorRepository).findById(99L);
        verify(doctorRepository, never()).save(any());
    }
    @Test
    void updateDoctor_ShouldThrowException_WhenDoctorDoesntExist() {
        given(doctorRepository.findById(1L)).willReturn(Optional.empty());

        DoctorDTO updatedDTO = new DoctorDTO(
                1L,
                "James",
                "Bond",
                "Dentist"
        );

        assertThrows(RuntimeException.class, () -> {
            doctorService.updateDoctor(1L, updatedDTO);
        });

        verify(doctorRepository, never()).save(any());
    }
    @Test
    public void deleteDoctor_ShouldCallSoftDelete() {
        // Given
        Doctor doctor = createTestDoctor(1L, "Иван", "Потапов", "кардиолог");
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        doNothing().when(doctorRepository).softDelete(1L);

        // When
        doctorService.deleteDoctor(1L);

        // Then
        verify(doctorRepository).findById(1L);
        verify(doctorRepository).softDelete(1L);
    }
    @Test
    void deleteDoctor_ShouldThrowException_WhenDoctorDoesntExist() {
        given(doctorRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            doctorService.deleteDoctor(1L);
        });

        verify(doctorRepository, never()).softDelete(anyLong());
    }
    @Test
    void deleteDoctor_ShouldSoftDeleteDoctor_WhenDoctorExists() {
        given(doctorRepository.findById(1L)).willReturn(Optional.of(createTestDoctor(1L, "John", "Doe", "cardiologist")));

        doctorService.deleteDoctor(1L);

        verify(doctorRepository, times(1)).softDelete(1L);
    }
    @Test
    public void deleteDoctor_WithInvalidId_ShouldThrowException() {
        // Given
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> doctorService.deleteDoctor(99L));
        verify(doctorRepository).findById(99L);
        verify(doctorRepository, never()).softDelete(any());
    }

    @Test
    public void convertToDto_ShouldReturnCorrectDto() {
        // Given
        Doctor doctor = createTestDoctor(1L, "Иван", "Потапов", "кардиолог");
        DoctorDTO expectedDTO = createTestDoctorDTO(1L, "Иван", "Потапов", "кардиолог");

        when(modelMapper.map(doctor, DoctorDTO.class)).thenReturn(expectedDTO);

        // When
        DoctorDTO result = doctorService.convertToDto(doctor);

        // Then
        assertThat(result, samePropertyValuesAs(expectedDTO));
        verify(modelMapper).map(doctor, DoctorDTO.class);
    }

    @Test
    public void convertToEntity_ShouldReturnCorrectEntity() {
        // Given
        DoctorDTO doctorDTO = createTestDoctorDTO(1L, "Иван", "Потапов", "кардиолог");
        Doctor expectedDoctor = createTestDoctor(1L, "Иван", "Потапов", "кардиолог");

        when(modelMapper.map(doctorDTO, Doctor.class)).thenReturn(expectedDoctor);

        // When
        Doctor result = doctorService.convertToEntity(doctorDTO);

        // Then
        assertThat(result, samePropertyValuesAs(expectedDoctor));
        verify(modelMapper).map(doctorDTO, Doctor.class);
    }
}