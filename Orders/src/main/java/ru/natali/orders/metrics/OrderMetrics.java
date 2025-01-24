package ru.natali.orders.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.natali.orders.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class OrderMetrics {

    private final OrderRepository orderRepository;
    private final MeterRegistry meterRegistry;

    private AtomicInteger orderCount = new AtomicInteger(0);
    private AtomicLong totalOrderAmount = new AtomicLong(0);

    public OrderMetrics(OrderRepository orderRepository, MeterRegistry meterRegistry) {
        this.orderRepository = orderRepository;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        // Метрика для количества заказов
        Gauge.builder("orders.count", orderCount, AtomicInteger::get)
                .description("Total number of orders")
                .register(meterRegistry);

        // Метрика для средней суммы заказа
        Gauge.builder("orders.avg_amount", () -> {
                    if (orderCount.get() == 0) return 0;
                    return totalOrderAmount.get() / orderCount.get();
                })
                .description("Average order amount")
                .register(meterRegistry);
    }

    public void incrementOrderCount() {
        orderCount.incrementAndGet();
    }

    public void addOrderAmount(BigDecimal amount) {
        totalOrderAmount.addAndGet(amount.longValue());
    }
}
