package ru.natali.pizzeria.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.natali.pizzeria.dto.CustomerDTO;
import ru.natali.pizzeria.model.Customer;

import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerMapperTest {

    @Autowired
    private CustomerMapper customerMapper;

    @Test
    void testToDto() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setPhone("+123456789");
        customer.setAddress("123 Main St");
        customer.setEmail("john@example.com");
        customer.setRegistrationDate(ZonedDateTime.now());
        customer.setActive(true);

        CustomerDTO dto = customerMapper.toDto(customer);

        assertEquals(customer.getId(), dto.getId());
        assertEquals(customer.getName(), dto.getName());
        assertEquals(customer.getPhone(), dto.getPhone());
        assertEquals(customer.getAddress(), dto.getAddress());
        assertEquals(customer.getEmail(), dto.getEmail());
        assertEquals(customer.getRegistrationDate(), dto.getRegistrationDate());
        assertEquals(customer.getActive(), dto.getActive());
    }

    @Test
    void testToEntity() {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(1L);
        dto.setName("John Doe");
        dto.setPhone("+123456789");
        dto.setAddress("123 Main St");
        dto.setEmail("john@example.com");
        dto.setRegistrationDate(ZonedDateTime.now());
        dto.setActive(true);

        Customer customer = customerMapper.toEntity(dto);

        assertEquals(dto.getId(), customer.getId());
        assertEquals(dto.getName(), customer.getName());
        assertEquals(dto.getPhone(), customer.getPhone());
        assertEquals(dto.getAddress(), customer.getAddress());
        assertEquals(dto.getEmail(), customer.getEmail());
        assertEquals(dto.getRegistrationDate(), customer.getRegistrationDate());
        assertEquals(dto.getActive(), customer.getActive());
    }

    @Test
    void testUpdateCustomerFromDto() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Old Name");
        customer.setPhone("+111111111");
        customer.setActive(false);

        CustomerDTO dto = new CustomerDTO();
        dto.setName("New Name");
        dto.setEmail("new@example.com");
        dto.setActive(true);

        customerMapper.updateCustomerFromDto(dto, customer);

        assertEquals(1L, customer.getId());
        assertEquals("New Name", customer.getName());
        assertEquals("+111111111", customer.getPhone());
        assertEquals("new@example.com", customer.getEmail());
        assertTrue(customer.getActive());
    }
}
