package ru.natali.medregistry.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.natali.medregistry.dto.DoctorDTO;
import ru.natali.medregistry.model.Doctor;
import ru.natali.medregistry.repository.DoctorRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class DoctorServiceImplTest {

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorService doctorService;

    private DoctorDTO testDoctorDTO;
    private Doctor testDoctor;

    @BeforeEach
    void setUp() {
        testDoctorDTO = new DoctorDTO(
                1L,
                "Alexander",
                "Smith",
                "Cardiologist"
        );

        testDoctor = new Doctor();
        testDoctor.setId(1L);
        testDoctor.setFirstName("Alexander");
        testDoctor.setLastName("Smith");
        testDoctor.setSpecialization("Cardiologist");
        testDoctor.setDeleted(false);
    }

    /* Тестирование метода создания */
   @Test
    void createDoctor_ShouldReturnSavedDoctor_WhenValidInputProvided() {
        // Дано, что уникальный доктор может быть сохранён
        given(doctorRepository.findById(1L)).willReturn(Optional.empty());
        given(doctorRepository.save(any(Doctor.class))).willReturn(testDoctor);

        // Действие: создаем нового доктора
        DoctorDTO result = doctorService.createDoctor(testDoctorDTO);

        // Утверждение: проверяем корректность результата
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(1L));
        assertThat(result.getFirstName(), equalTo("Alexander"));
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

   /*
   //Тест проверяет наличие такого id перед сохранением и начинает глючить контроллер, там совсем не нужно
    // проверять id  перед сохранением, т.к. автоинкремент
   @Test
    void createDoctor_ShouldThrowException_WhenDuplicateDoctorExists() {
        // Предположим, что такой доктор уже существует
        given(doctorRepository.findById(1L)).willReturn(Optional.of(testDoctor));

        // Действие: пытаемся создать дубликат
        assertThrows(RuntimeException.class, () -> {
            doctorService.createDoctor(testDoctorDTO);
        });

        // Проверка: сохранение не происходило
        verify(doctorRepository, never()).save(any(Doctor.class));
    }*/

    /* Тестирование метода получения всех докторов */
    @Test
    void getAllDoctors_ShouldReturnListOfDoctors() {
        given(doctorRepository.findAll()).willReturn(Collections.singletonList(testDoctor));

        List<DoctorDTO> result = doctorService.getAllDoctors();

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getFirstName(), equalTo("Alexander"));
        verify(doctorRepository, times(1)).findAll();
    }

    /* Тестирование метода обновления доктора */
    @Test
    void updateDoctor_ShouldReturnUpdatedDoctor_WhenValidInputProvided() {
        given(doctorRepository.findById(1L)).willReturn(Optional.of(testDoctor));
        given(doctorRepository.save(any(Doctor.class))).willReturn(testDoctor);

        DoctorDTO updatedDTO = new DoctorDTO(
                1L,
                "James",
                "Bond",
                "Dentist"
        );

        DoctorDTO result = doctorService.updateDoctor(1L, updatedDTO);

        assertThat(result.getFirstName(), equalTo("James"));
        assertThat(result.getSpecialization(), equalTo("Dentist"));
        verify(doctorRepository, times(1)).save(any(Doctor.class));
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

        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    /* Тестирование метода удаления доктора */
    @Test
    void deleteDoctor_ShouldSoftDeleteDoctor_WhenDoctorExists() {
        given(doctorRepository.findById(1L)).willReturn(Optional.of(testDoctor));

        doctorService.deleteDoctor(1L);

        verify(doctorRepository, times(1)).softDelete(1L);
    }

    @Test
    void deleteDoctor_ShouldThrowException_WhenDoctorDoesntExist() {
        given(doctorRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            doctorService.deleteDoctor(1L);
        });

        verify(doctorRepository, never()).softDelete(anyLong());
    }

}
