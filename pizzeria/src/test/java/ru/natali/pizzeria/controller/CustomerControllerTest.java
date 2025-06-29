package ru.natali.pizzeria.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.natali.pizzeria.config.WebSecurityConfig;
import ru.natali.pizzeria.dto.CustomerDTO;
import ru.natali.pizzeria.exception.ConflictException;
import ru.natali.pizzeria.exception.EntityNotFoundException;
import ru.natali.pizzeria.service.CustomerService;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import(WebSecurityConfig.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private final CustomerDTO testCustomer = new CustomerDTO(
            1L, "John Doe", "123456789", "Address", "john@example.com", true, ZonedDateTime.now());

    @Test
    @WithMockUser
    void createCustomer_ShouldReturnCreatedCustomer() throws Exception {
        given(customerService.createCustomer(any(CustomerDTO.class))).willReturn(testCustomer);

        mockMvc.perform(post("/api/customers")
                        .with(csrf()) // Добавляем CSRF токен
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "John Doe",
                            "phone": "123456789",
                            "address": "Address",
                            "email": "john@example.com"
                        }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void createCustomer_WhenUnauthenticated_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest()); // Ожидаем 400 из-за валидации
    }

    @Test
    @WithMockUser
    void createCustomer_WhenInvalidData_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "",
                            "phone": "",
                            "address": ""
                        }"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    @WithMockUser
    void getCustomer_ShouldReturnCustomer() throws Exception {
        given(customerService.getCustomer(1L)).willReturn(testCustomer);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser
    void getCustomer_WhenNotFound_ShouldReturn404() throws Exception {
        given(customerService.getCustomer(1L)).willThrow(new EntityNotFoundException("Customer", 1L));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Customer not found with id: 1"));
    }

    @Test
    @WithMockUser
    void getAllCustomers_ShouldReturnCustomersList() throws Exception {
        given(customerService.getAllCustomers()).willReturn(List.of(testCustomer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    @WithMockUser
    void updateCustomer_ShouldReturnUpdatedCustomer() throws Exception {
        CustomerDTO updatedCustomer = new CustomerDTO(1L, "John Updated", "123456789", "New Address", "john@example.com", true, ZonedDateTime.now());
        given(customerService.updateCustomer(eq(1L), any(CustomerDTO.class))).willReturn(updatedCustomer);

        mockMvc.perform(put("/api/customers/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "John Updated",
                            "phone": "123456789",
                            "address": "New Address",
                            "email": "john@example.com"
                        }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.address").value("New Address"));
    }

    @Test
    @WithMockUser
    void updateCustomer_WhenConflict_ShouldReturn409() throws Exception {
        given(customerService.updateCustomer(eq(1L), any(CustomerDTO.class)))
                .willThrow(new ConflictException("Customer email already exists"));

        mockMvc.perform(put("/api/customers/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "name": "Valid Name",
                        "phone": "123456789",
                        "address": "Valid Address",
                        "email": "valid@example.com"
                    }"""))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONFLICT"));
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Тест с пользователем, имеющим роль ADMIN
    void deleteCustomer_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/customers/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Пользователь с ролью ADMIN
    void deleteCustomer_WhenAdmin_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/customers/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCustomer_WhenNotFound_ShouldReturn404() throws Exception {
        doThrow(new EntityNotFoundException("Customer", 1L)).when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
