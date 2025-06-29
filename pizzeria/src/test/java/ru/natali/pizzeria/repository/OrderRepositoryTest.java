package ru.natali.pizzeria.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.natali.pizzeria.model.Customer;
import ru.natali.pizzeria.model.Order;
import ru.natali.pizzeria.model.OrderItem;
import ru.natali.pizzeria.model.Pizza;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Customer testCustomer;
    private Pizza pizza1, pizza2;
    private ZonedDateTime testDate;

    @BeforeEach
    void setUp() {

        entityManager.clear();

        testCustomer = new Customer();
        testCustomer.setName("Test Customer");
        testCustomer.setPhone("+1234567890");
        testCustomer.setAddress("Test Address");
        testCustomer.setEmail("test@example.com");
        testCustomer = customerRepository.save(testCustomer);

        pizza1 = new Pizza();
        pizza1.setName("Margherita");
        pizza1.setPrice(450.0);
        pizza1.setDescription("Classic margherita");
        pizza1.setAvailable(true);
        pizza1 = pizzaRepository.save(pizza1);

        pizza2 = new Pizza();
        pizza2.setName("Pepperoni");
        pizza2.setPrice(300.0);
        pizza2.setDescription("Spicy pepperoni");
        pizza2.setAvailable(true);
        pizza2 = pizzaRepository.save(pizza2);

        testDate = ZonedDateTime.of(2023, 1, 1, 12, 0, 0, 0, ZoneId.systemDefault());
    }

   @Test
   void saveOrder_WithItems_ShouldCorrectlyPersist() {

       Order order = createOrderWithItems(pizza1, 2);

       OrderItem item2 = new OrderItem();
       item2.setPizza(pizza2);
       item2.setQuantity(1);
       item2.setPrice(300.0);
       item2.setSpecialRequests("Без лука");
       item2.setOrder(order);

       order.getItems().add(item2);
       order.setTotalPrice(order.getTotalPrice() + item2.getPrice());

       Order savedOrder = orderRepository.save(order);
       entityManager.flush();
       entityManager.clear();

       List<OrderItem> items = getOrderItems(savedOrder.getId());
       assertEquals(2, items.size());
   }

    @Test
    void deleteOrder_ShouldCascadeDeleteItems() {
    Order order = createOrderWithItems(pizza1, 1);
    Long orderId = order.getId();

    orderRepository.deleteById(orderId);
    entityManager.flush();

    List<OrderItem> items = getOrderItems(orderId);
    assertTrue(items.isEmpty());
    }

    @Test
    void findByCustomerId_ShouldReturnOnlyCustomersOrders() {

        Customer anotherCustomer = new Customer();
        anotherCustomer.setName("Another Customer");
        anotherCustomer.setPhone("+0987654321");
        anotherCustomer.setAddress("Another Address");
        anotherCustomer = customerRepository.save(anotherCustomer);

        createTestOrder(testCustomer.getId(), "NEW", 500.0);
        createTestOrder(anotherCustomer.getId(), "NEW", 600.0);

        List<Order> customerOrders = orderRepository.findByCustomerId(testCustomer.getId());

        assertEquals(1, customerOrders.size());
        assertEquals(testCustomer.getId(), customerOrders.get(0).getCustomer().getId());
    }

    @Test
    void findByOrderDateBetween_ShouldFilterCorrectly() {

        Order oldOrder = createTestOrder("DELIVERED", 400.0);
        oldOrder.setOrderDate(testDate.minusDays(2));
        orderRepository.save(oldOrder);

        Order currentOrder = createTestOrder("NEW", 500.0);
        currentOrder.setOrderDate(testDate);
        orderRepository.save(currentOrder);

        Order futureOrder = createTestOrder("PROCESSING", 600.0);
        futureOrder.setOrderDate(testDate.plusDays(2));
        orderRepository.save(futureOrder);

        List<Order> result = orderRepository.findByOrderDateBetween(
                testDate.minusDays(1),
                testDate.plusDays(1)
        );

        assertEquals(1, result.size());
        assertEquals(currentOrder.getId(), result.get(0).getId());
    }

    @Test
    void updateOrder_ShouldCorrectlyUpdateFields() {
        Order order = createBasicOrder();
        order = orderRepository.save(order);

        order.setStatus("DELIVERED");
        order.setPaymentStatus("COMPLETED");
        order.setDeliveryNotes("Оставить у двери");

        orderRepository.save(order);
        entityManager.flush();
        entityManager.clear();

        Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow();

        assertAll(
                () -> assertEquals("DELIVERED", updatedOrder.getStatus()),
                () -> assertEquals("COMPLETED", updatedOrder.getPaymentStatus()),
                () -> assertEquals("Оставить у двери", updatedOrder.getDeliveryNotes())
        );
    }

    @Test
    void findByStatus_ShouldReturnCorrectOrders() {

        orderRepository.deleteAll();
        entityManager.flush();

        Order order1 = createBasicOrder();
        order1.setStatus("NEW");
        orderRepository.save(order1);

        Order order2 = createBasicOrder();
        order2.setStatus("PROCESSING");
        orderRepository.save(order2);

        Long expectedCustomerId = order1.getCustomer().getId();

        List<Order> newOrders = orderRepository.findByStatus("NEW");

        assertAll(
                () -> assertEquals(1, newOrders.size(),
                        "Должен быть только один заказ со статусом NEW"),
                () -> assertEquals("NEW", newOrders.get(0).getStatus(),
                        "Статус заказа должен быть NEW"),
                () -> assertEquals(expectedCustomerId, newOrders.get(0).getCustomer().getId(),
                        "ID клиента должно совпадать с ожидаемым")
        );
    }

    @Test
    void orderTotalPrice_ShouldBeCalculatedCorrectly() {

    Order order = createOrderWithItems(pizza1, 2);

    OrderItem item2 = new OrderItem();
    item2.setPizza(pizza2);
    item2.setQuantity(3);
    item2.setPrice(pizza2.getPrice() * 3);
    item2.setOrder(order);

    order.getItems().add(item2);
    order.setTotalPrice(order.getTotalPrice() + item2.getPrice());

    Order savedOrder = orderRepository.save(order);

    assertEquals(1800.0, savedOrder.getTotalPrice());
    }

    private List<OrderItem> getOrderItems(Long orderId) {
        return entityManager.getEntityManager()
                .createQuery("SELECT i FROM OrderItem i WHERE i.order.id = :orderId", OrderItem.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private Order createOrderWithItems(Pizza pizza, int quantity) {
        Order order = createBasicOrder();

        OrderItem item = new OrderItem();
        item.setPizza(pizza);
        item.setQuantity(quantity);
        item.setPrice(pizza.getPrice() * quantity);
        item.setOrder(order);

        order.getItems().add(item);
        order.setTotalPrice(pizza.getPrice() * quantity);

        return orderRepository.save(order);
    }

    private Order createTestOrder(String status, Double totalPrice) {
        return createTestOrder(testCustomer.getId(), status, totalPrice);
    }

    private Order createTestOrder(Long customerId, String status, Double totalPrice) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(status);
        order.setTotalPrice(totalPrice);
        order.setDeliveryAddress("Test Address");
        order.setPaymentMethod("CASH");
        order.setPaymentStatus("PENDING");
        order.setOrderDate(ZonedDateTime.now());

        return orderRepository.save(order);
    }

    private Order createBasicOrder() {
        Order order = new Order();
        order.setCustomer(testCustomer);
        order.setStatus("NEW");
        order.setTotalPrice(0.0);
        order.setDeliveryAddress("Test Address");
        order.setPaymentMethod("CARD");
        order.setPaymentStatus("PENDING");
        order.setOrderDate(ZonedDateTime.now());
        return order;
    }
}