package ru.natali.pizzeria.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.natali.pizzeria.dto.EmployeeDTO;
import ru.natali.pizzeria.model.Employee;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeMapperTest {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    void testToDto() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Alice Smith");
        employee.setPosition("Manager");
        employee.setPhone("+987654321");
        employee.setEmail("alice@example.com");
        employee.setHireDate(LocalDate.of(2020, 1, 15));
        employee.setActive(true);

        EmployeeDTO dto = employeeMapper.toDto(employee);

        assertEquals(employee.getId(), dto.getId());
        assertEquals(employee.getName(), dto.getName());
        assertEquals(employee.getPosition(), dto.getPosition());
        assertEquals(employee.getPhone(), dto.getPhone());
        assertEquals(employee.getEmail(), dto.getEmail());
        assertEquals(employee.getHireDate(), dto.getHireDate());
        assertEquals(employee.getActive(), dto.getActive());
    }

    @Test
    void testToEntity() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(1L);
        dto.setName("Alice Smith");
        dto.setPosition("Manager");
        dto.setPhone("+987654321");
        dto.setEmail("alice@example.com");
        dto.setHireDate(LocalDate.of(2020, 1, 15));
        dto.setActive(true);

        Employee employee = employeeMapper.toEntity(dto);

        assertEquals(dto.getId(), employee.getId());
        assertEquals(dto.getName(), employee.getName());
        assertEquals(dto.getPosition(), employee.getPosition());
        assertEquals(dto.getPhone(), employee.getPhone());
        assertEquals(dto.getEmail(), employee.getEmail());
        assertEquals(dto.getHireDate(), employee.getHireDate());
        assertEquals(dto.getActive(), employee.getActive());
    }

    @Test
    void testUpdateEntity() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Old Name");
        employee.setActive(false);

        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("New Name");
        dto.setPosition("Chef");
        dto.setActive(true);

        employeeMapper.updateEntity(dto, employee);

        assertEquals(1L, employee.getId());
        assertEquals("New Name", employee.getName());
        assertEquals("Chef", employee.getPosition());
        assertTrue(employee.getActive());
        assertNull(employee.getPhone());
    }
}
