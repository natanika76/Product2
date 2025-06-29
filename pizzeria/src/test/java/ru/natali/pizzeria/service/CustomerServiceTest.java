package ru.natali.pizzeria.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.natali.pizzeria.dto.CustomerDTO;
import ru.natali.pizzeria.exception.EntityNotFoundException;
import ru.natali.pizzeria.mapper.CustomerMapper;
import ru.natali.pizzeria.model.Customer;
import ru.natali.pizzeria.repository.CustomerRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void createCustomer_ShouldReturnSavedCustomer() {

        ZonedDateTime registrationDate = ZonedDateTime.now();

        CustomerDTO inputDto = new CustomerDTO(
                null,
                "John Doe",
                "+123456789",
                "Address",
                "john@example.com",
                true,
                null
        );

        // Expected entity after saving
        Customer expectedEntity = new Customer(
                1L,
                "John Doe",
                "+123456789",
                "Address",
                "john@example.com",
                registrationDate,
                true
        );

        // Expected DTO to return
        CustomerDTO expectedDto = new CustomerDTO(
                1L,
                "John Doe",
                "+123456789",
                "Address",
                "john@example.com",
                true,
                registrationDate
        );

        when(customerMapper.toEntity(inputDto)).thenReturn(new Customer());
        when(customerRepository.save(any(Customer.class))).thenReturn(expectedEntity);
        when(customerMapper.toDto(expectedEntity)).thenReturn(expectedDto);

        CustomerDTO result = customerService.createCustomer(inputDto);

        assertEquals(expectedDto, result);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void getCustomer_ShouldReturnCustomer_WhenExists() {

        Long customerId = 1L;
        ZonedDateTime regDate = ZonedDateTime.now();
        Customer customer = new Customer(customerId, "Иван", "+79101112233", "ул. Ленина", "ivan@test.ru", regDate, true);
        CustomerDTO expectedDto = new CustomerDTO(customerId, "Иван", "+79101112233", "ул. Ленина", "ivan@test.ru", true, regDate);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(expectedDto);

        CustomerDTO result = customerService.getCustomer(customerId);

        assertEquals(expectedDto, result);
        verify(customerRepository).findById(customerId);
    }

    @Test
    void getCustomer_ShouldThrowException_WhenNotExists() {

        Long customerId = 999L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.getCustomer(customerId));
        verify(customerRepository).findById(customerId);
    }

    @Test
    void updateCustomer_ShouldUpdateFields() {

        Long customerId = 1L;
        ZonedDateTime oldRegDate = ZonedDateTime.now().minusDays(1);

        Customer existing = new Customer(customerId, "Старое имя", "+79101112233", "Старый адрес", "old@test.ru", oldRegDate, true);

        CustomerDTO updateDto = new CustomerDTO(customerId, "Новое имя", "+79102223344", "Новый адрес", "new@test.ru", false, null);

        CustomerDTO expectedDto = new CustomerDTO(customerId, "Новое имя", "+79102223344", "Новый адрес", "new@test.ru", false, oldRegDate);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any())).thenReturn(existing); // возвращаем обновлённую сущность
        when(customerMapper.toDto(existing)).thenReturn(expectedDto); // важно: mapper должен вернуть DTO!

        CustomerDTO result = customerService.updateCustomer(customerId, updateDto);

        assertNotNull(result);
        assertEquals("Новое имя", result.getName());
        assertEquals("+79102223344", result.getPhone());
        assertEquals("Новый адрес", result.getAddress());
        assertEquals("new@test.ru", result.getEmail());
        assertFalse(result.getActive());
        assertEquals(oldRegDate, result.getRegistrationDate());
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer_WhenExists() {

        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(customerId);

        customerService.deleteCustomer(customerId);

        verify(customerRepository).existsById(customerId);
        verify(customerRepository).deleteById(customerId);
    }

    @Test
    void deleteCustomer_ShouldThrowException_WhenNotExists() {

        Long customerId = 999L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomer(customerId));
        verify(customerRepository).existsById(customerId);
        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() {

        ZonedDateTime regDate = ZonedDateTime.now();
        Customer customer1 = new Customer(1L, "Иван", "+79101112233", "ул. Ленина", "ivan@test.ru", regDate, true);
        Customer customer2 = new Customer(2L, "Петр", "+79102223344", "ул. Пушкина", "petr@test.ru", regDate, true);

        CustomerDTO dto1 = new CustomerDTO(1L, "Иван", "+79101112233", "ул. Ленина", "ivan@test.ru", true, regDate);
        CustomerDTO dto2 = new CustomerDTO(2L, "Петр", "+79102223344", "ул. Пушкина", "petr@test.ru", true, regDate);

        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));
        when(customerMapper.toDto(customer1)).thenReturn(dto1);
        when(customerMapper.toDto(customer2)).thenReturn(dto2);

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
        verify(customerRepository).findAll();
    }

    @Test
    void findActiveCustomers_ShouldReturnOnlyActiveCustomers() {

        ZonedDateTime regDate = ZonedDateTime.now();
        Customer activeCustomer = new Customer(1L, "Иван", "+79101112233", "ул. Ленина", "ivan@test.ru", regDate, true);
        Customer inactiveCustomer = new Customer(2L, "Петр", "+79102223344", "ул. Пушкина", "petr@test.ru", regDate, false);

        CustomerDTO activeDto = new CustomerDTO(1L, "Иван", "+79101112233", "ул. Ленина", "ivan@test.ru", true, regDate);

        when(customerRepository.findByActiveTrue()).thenReturn(List.of(activeCustomer));
        when(customerMapper.toDto(activeCustomer)).thenReturn(activeDto);

        List<CustomerDTO> result = customerService.findActiveCustomers();

        assertEquals(1, result.size());
        assertEquals(activeDto, result.get(0));
        verify(customerRepository).findByActiveTrue();
    }

    @Test
    void updateCustomer_ShouldNotChangeRegistrationDate() {

        Long customerId = 1L;
        ZonedDateTime originalRegDate = ZonedDateTime.now().minusDays(5);
        Customer existing = new Customer(customerId, "Старое имя", "+79101112233", "Старый адрес", "old@test.ru", originalRegDate, true);

        ZonedDateTime newRegDate = ZonedDateTime.now();
        CustomerDTO updateDto = new CustomerDTO(customerId, "Новое имя", "+79102223344", "Новый адрес", "new@test.ru", false, newRegDate);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(existing);
        when(customerMapper.toDto(existing)).thenReturn(updateDto);

        CustomerDTO result = customerService.updateCustomer(customerId, updateDto);

        assertNotNull(result);
        assertEquals(originalRegDate, existing.getRegistrationDate()); // Дата не изменилась
        verify(customerRepository).save(existing);
    }
}