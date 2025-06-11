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
import ru.natali.medregistry.dto.DoctorDTO;
import ru.natali.medregistry.exceptions.DoctorNotFoundException;
import ru.natali.medregistry.model.Doctor;
import ru.natali.medregistry.repository.DoctorRepository;
import ru.natali.medregistry.service.DoctorService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Реализация сервиса для работы с врачами.
 * Обеспечивает бизнес-логику управления врачами, включая кэширование.
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "doctors") // Общие настройки кэша для класса
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    /**
     * Получает список всех врачей.
     *
     * @return список DTO врачей
     */
    @Override
    @Cacheable // Кэшируем результат метода
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получает врача по идентификатору.
     *
     * @param id идентификатор врача
     * @return DTO врача
     * @throws EntityNotFoundException если врач не найден
     */
    @Override
    @Cacheable(key = "#id") // Кэшируем по id доктора
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));
        return convertToDto(doctor);
    }

    /**
     * Создает нового врача.
     *
     * @param doctorDTO DTO врача для создания
     * @return созданный DTO врача
     * @throws RuntimeException если врач с таким ID уже существует
     */
    /*@Override
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = convertToEntity(doctorDTO);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return convertToDto(savedDoctor);
    }
*/  @Override
    @CacheEvict(allEntries = true) // Очищаем весь кэш при добавлении нового доктора
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {

        Doctor doctor = convertToEntity(doctorDTO);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return convertToDto(savedDoctor);
    }

    /**
     * Обновляет данные врача.
     *
     * @param id идентификатор врача
     * @param doctorDTO обновленные данные врача
     * @return обновленный DTO врача
     * @throws DoctorNotFoundException если врач не найден
     */
    @Override
    public DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));

        modelMapper.map(doctorDTO, existingDoctor);
        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        return convertToDto(updatedDoctor);
    }

    /**
     * Удаляет врача (мягкое удаление).
     *
     * @param id идентификатор врача для удаления
     * @throws EntityNotFoundException если врач не найден
     */
    @Override
    @Transactional
    @CacheEvict(key = "#id") // Удаляем запись из кэша при удалении доктора
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));
        doctorRepository.softDelete(id);
    }

    /**
     * Конвертирует сущность врача в DTO.
     *
     * @param doctor сущность врача
     * @return DTO врача
     */
    @Override
    public DoctorDTO convertToDto(Doctor doctor) {
        return modelMapper.map(doctor, DoctorDTO.class);
    }

    /**
     * Конвертирует DTO врача в сущность.
     *
     * @param doctorDTO DTO врача
     * @return сущность врача
     */
    @Override
    public Doctor convertToEntity(DoctorDTO doctorDTO) {
        return modelMapper.map(doctorDTO, Doctor.class);
    }
}
