package ru.natali.pizzeria.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import ru.natali.pizzeria.model.Customer;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Customer activeCustomer;
    private Customer inactiveCustomer;

    @BeforeEach
    void setUp() {
        // Очистка данных
        orderRepository.deleteAll();
        customerRepository.deleteAll();

        // Создание тестовых данных
        activeCustomer = new Customer();
        activeCustomer.setName("Active Customer");
        activeCustomer.setPhone("+123456789");
        activeCustomer.setAddress("Address 1");
        activeCustomer.setEmail("active@test.com");
        activeCustomer.setRegistrationDate(ZonedDateTime.now());
        activeCustomer.setActive(true);

        inactiveCustomer = new Customer();
        inactiveCustomer.setName("Inactive Customer");
        inactiveCustomer.setPhone("+987654321");
        inactiveCustomer.setAddress("Address 2");
        inactiveCustomer.setEmail("inactive@test.com");
        inactiveCustomer.setRegistrationDate(ZonedDateTime.now());
        inactiveCustomer.setActive(false);

        customerRepository.saveAll(List.of(activeCustomer, inactiveCustomer));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findAll_ShouldReturnAllCustomers() {
        List<Customer> result = customerRepository.findAll();

        assertEquals(2, result.size());
        assertThat(result).extracting(Customer::getName)
                .containsExactlyInAnyOrder("Active Customer", "Inactive Customer");
    }

    @Test
    void findById_ShouldReturnCustomer_WhenExists() {
        Optional<Customer> result = customerRepository.findById(activeCustomer.getId());

        assertTrue(result.isPresent());
        assertEquals("Active Customer", result.get().getName());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Customer> result = customerRepository.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void findByActiveTrue_ShouldReturnOnlyActiveCustomers() {
        List<Customer> result = customerRepository.findByActiveTrue();

        assertEquals(1, result.size());
        assertEquals("Active Customer", result.get(0).getName());
        assertTrue(result.get(0).getActive());
    }

    @Test
    void findByPhone_ShouldReturnCustomer_WhenExists() {
        Optional<Customer> result = customerRepository.findByPhone("+123456789");

        assertTrue(result.isPresent());
        assertEquals("Active Customer", result.get().getName());
    }

    @Test
    void findByPhone_ShouldReturnEmpty_WhenNotExists() {
        Optional<Customer> result = customerRepository.findByPhone("+000000000");

        assertFalse(result.isPresent());
    }

    @Test
    void save_ShouldCreateNewCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.setName("New Customer");
        newCustomer.setPhone("+111111111");
        newCustomer.setAddress("New Address");
        newCustomer.setEmail("new@test.com");
        newCustomer.setActive(true);

        Customer saved = customerRepository.save(newCustomer);

        assertNotNull(saved.getId());
        assertEquals("New Customer", saved.getName());
        assertEquals(3, customerRepository.count());
    }

    @Test
    void save_ShouldUpdateExistingCustomer() {
        activeCustomer.setName("Updated Name");
        Customer updated = customerRepository.save(activeCustomer);

        assertEquals(activeCustomer.getId(), updated.getId());
        assertEquals("Updated Name", updated.getName());
        assertEquals(2, customerRepository.count());
    }

    @Test
    void delete_ShouldRemoveCustomer() {
        customerRepository.delete(activeCustomer);

        assertEquals(1, customerRepository.count());
        assertFalse(customerRepository.findById(activeCustomer.getId()).isPresent());
    }

    @Test
    void existsById_ShouldReturnTrue_WhenCustomerExists() {
        boolean exists = customerRepository.existsById(activeCustomer.getId());

        assertTrue(exists);
    }

    @Test
    void existsById_ShouldReturnFalse_WhenCustomerNotExists() {
        boolean exists = customerRepository.existsById(999L);

        assertFalse(exists);
    }

    @Test
    void count_ShouldReturnCorrectNumberOfCustomers() {
        long count = customerRepository.count();

        assertEquals(2, count);
    }

    @Test
    void findByEmail_ShouldReturnCustomer_WhenEmailExists() {

        Optional<Customer> result = customerRepository.findByEmail("active@test.com");

        assertTrue(result.isPresent());
        assertEquals("Active Customer", result.get().getName());
    }

    @Test
    void save_ShouldThrowException_WhenPhoneIsNull() {
        Customer customer = new Customer();
        customer.setName("Test");
        customer.setAddress("Address");
        customer.setEmail("test@test.com");
        // phone не установлен

        assertThrows(DataIntegrityViolationException.class, () -> {
            customerRepository.saveAndFlush(customer);
        });
    }

    @Test
    void findByRegistrationDateBetween_ShouldReturnFilteredCustomers() {

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime start = now.minusDays(1);
        ZonedDateTime end = now.plusDays(1);

        List<Customer> result = customerRepository.findByRegistrationDateBetween(start, end);

        assertEquals(2, result.size());
    }

    @Test
    void findByPhone_ShouldReturnCustomer() {

        String phone = "+79101112233";
        Customer customer = new Customer(null, "Иван", phone, "ул. Ленина", "ivan@test.ru", ZonedDateTime.now(), true);
        customerRepository.save(customer);

        Optional<Customer> result = customerRepository.findByPhone(phone);

        assertTrue(result.isPresent());
        assertEquals("Иван", result.get().getName());
    }

    @Test
    void findAllByOrderByNameAsc_ShouldReturnSortedResults() {

        List<Customer> result = customerRepository.findAllByOrderByNameAsc();

        assertEquals("Active Customer", result.get(0).getName());
        assertEquals("Inactive Customer", result.get(1).getName());
    }

    @Test
    void findActiveCustomersRegisteredAfter_ShouldReturnFilteredResults() {
        ZonedDateTime yesterday = ZonedDateTime.now().minusDays(1);
        activeCustomer.setRegistrationDate(yesterday.plusHours(1));
        customerRepository.save(activeCustomer);

        List<Customer> result = customerRepository.findActiveCustomersRegisteredAfter(yesterday);

        assertEquals(1, result.size());
        assertEquals("Active Customer", result.get(0).getName());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenEmailNotExists() {
        Optional<Customer> result = customerRepository.findByEmail("wrong@email.com");
        assertFalse(result.isPresent());
    }

    @Test
    void findAllByOrderByNameAsc_ShouldReturnCorrectOrder() {

        customerRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        Customer customerB = new Customer();
        customerB.setName("B");
        customerB.setPhone("+2");
        customerB.setAddress("AddressB");
        customerB.setEmail("b@test.com");
        customerB.setActive(true);

        Customer customerA = new Customer();
        customerA.setName("A");
        customerA.setPhone("+1");
        customerA.setEmail("a@test.com");
        customerA.setAddress("AddressA");
        customerA.setActive(true);

        customerRepository.saveAll(List.of(customerB, customerA));
        entityManager.flush();
        entityManager.clear();

        List<Customer> result = customerRepository.findAllByOrderByNameAsc();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("B", result.get(1).getName());
    }

    @Test
    void findTop5ByOrderByRegistrationDateDesc_ShouldReturnLatestCustomers() {

        for (int i = 0; i < 10; i++) {
            Customer c = new Customer();
            c.setName("Customer " + i);
            c.setPhone("+phone" + i);
            c.setAddress("Address " + i);
            c.setEmail("email" + i + "@test.com");
            c.setRegistrationDate(ZonedDateTime.now().minusDays(i));
            customerRepository.save(c);
        }
        entityManager.flush();

        List<Customer> result = customerRepository.findTop5ByOrderByRegistrationDateDesc();

        assertEquals(5, result.size());
        assertTrue(result.get(0).getRegistrationDate()
                .isAfter(result.get(1).getRegistrationDate()));
    }

    @Test
    void save_ShouldFail_WhenEmailIsInvalid() {
        // Arrange
        Customer invalidCustomer = new Customer();
        invalidCustomer.setName("Invalid Email");
        invalidCustomer.setPhone("+123456789");
        invalidCustomer.setAddress("Address");
        invalidCustomer.setEmail("invalid-email");
        invalidCustomer.setActive(true);

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> {
            customerRepository.save(invalidCustomer);
            entityManager.flush();
        });
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnMatchingCustomers() {

        customerRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        Customer targetCustomer = new Customer();
        targetCustomer.setName("Active Customer");
        targetCustomer.setPhone("+111111111");
        targetCustomer.setAddress("Address1");
        targetCustomer.setEmail("target@test.com");
        targetCustomer.setActive(true);

        Customer similarCustomer = new Customer();
        similarCustomer.setName("Customer Active");
        similarCustomer.setPhone("+222222222");
        similarCustomer.setAddress("Address2");
        similarCustomer.setEmail("similar@test.com");
        similarCustomer.setActive(true);

        Customer unrelatedCustomer = new Customer();
        unrelatedCustomer.setName("Other User");
        unrelatedCustomer.setPhone("+333333333");
        unrelatedCustomer.setAddress("Address3");
        unrelatedCustomer.setEmail("other@test.com");
        unrelatedCustomer.setActive(false);

        customerRepository.saveAll(List.of(targetCustomer, similarCustomer, unrelatedCustomer));
        entityManager.flush();
        entityManager.clear();

        List<Customer> result = customerRepository.findByNameContainingIgnoreCase("Active Cus");

        assertEquals(1, result.size(), "Должен вернуться только один точный результат");
        assertEquals("Active Customer", result.get(0).getName());
    }
}