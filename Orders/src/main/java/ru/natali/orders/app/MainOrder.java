package ru.natali.orders.app;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.natali.orders.model.Order;
import ru.natali.orders.repository.OrderRepository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class MainOrder {
    public static void main(String[] args) {

        //Объект DataSource предоставляет Connection(интерфейс), который предоставляет доступ к БД
        DataSource dataSource = new DriverManagerDataSource("jdbc:postgresql://localhost:5433/products",
                "postgres", "Lzmf2000");

        // Создание репозитория
        OrderRepository orderRepository = new OrderRepository(dataSource);

        // Тестовые операции
        //testOrderRepository(orderRepository);
    }
    private static void testOrderRepository(OrderRepository orderRepository) {

        // Создание заказа
        Order order = new Order();
        order.setProductArticle("ART12345");
        order.setQuantity(2);
        order.setTotalAmount(new BigDecimal("1999.98"));
        order.setOrderDate(LocalDateTime.now());

        // Сохранение заказа
        Long orderId = orderRepository.save(order);
        System.out.println("Создан заказ с ID: " + orderId);

        // Получение заказа по ID
        Order savedOrder = orderRepository.findById(orderId);
        System.out.println("Найден заказ: " + savedOrder);

        // Получение всех заказов
        List<Order> orders = orderRepository.findAll();
        System.out.println("Все заказы: " + orders);

        // Обновление заказа
        savedOrder.setQuantity(3);
        orderRepository.update(savedOrder);
        System.out.println("Заказ обновлён: " + orderRepository.findById(orderId));

        // Удаление заказа
        orderRepository.delete(orderId);
        System.out.println("Заказ удалён.");
    }
}
