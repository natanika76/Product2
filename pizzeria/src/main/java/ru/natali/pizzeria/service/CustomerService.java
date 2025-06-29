package ru.natali.pizzeria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.natali.pizzeria.dto.CustomerDTO;

import ru.natali.pizzeria.exception.EntityNotFoundException;
import ru.natali.pizzeria.mapper.CustomerMapper;
import ru.natali.pizzeria.model.Customer;
import ru.natali.pizzeria.repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        return customerMapper.toDto(customerRepository.save(customer));
    }

    public CustomerDTO getCustomer(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Customer", id));
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer", id)); // Используем ваш конструктор

        customerMapper.updateCustomerFromDto(customerDTO, existingCustomer);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toDto(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer", id); // Используем ваш конструктор
        }
        customerRepository.deleteById(id);
    }

    // Дополнительные методы по необходимости
    public List<CustomerDTO> findActiveCustomers() {
        return customerRepository.findByActiveTrue()
                .stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }
}
