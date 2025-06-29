package ru.natali.pizzeria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natali.pizzeria.dto.EmployeeDTO;
import ru.natali.pizzeria.mapper.EmployeeMapper;
import ru.natali.pizzeria.model.Employee;
import ru.natali.pizzeria.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public List<EmployeeDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public EmployeeDTO findById(Long id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id)));
    }

    @Transactional
    public EmployeeDTO create(EmployeeDTO dto) {
        Employee employee = mapper.toEntity(dto);
        employee.setActive(true); // По умолчанию активен
        return mapper.toDto(repository.save(employee));
    }

    @Transactional
    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        mapper.updateEntity(dto, existing);
        return mapper.toDto(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
