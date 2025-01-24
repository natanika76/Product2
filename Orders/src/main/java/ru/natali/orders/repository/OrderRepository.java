package ru.natali.orders.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.natali.orders.model.Order;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepository {
    private JdbcTemplate jdbcTemplate;

    public OrderRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Long save(Order order) {
        String sql = "INSERT INTO orders (product_article, quantity, total_amount, order_date) VALUES (?, ?, ?, ?) RETURNING order_id";
        return jdbcTemplate.queryForObject(sql, Long.class,
                order.getProductArticle(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getOrderDate());
    }

    public Order findById(Long orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), orderId);
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    public void update(Order order) {
        String sql = "UPDATE orders SET product_article = ?, quantity = ?, total_amount = ?, order_date = ? WHERE order_id = ?";
        jdbcTemplate.update(sql,
                order.getProductArticle(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getOrderDate(),
                order.getOrderId());
    }

    public void delete(Long orderId) {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        jdbcTemplate.update(sql, orderId);
    }

    private static class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setOrderId(rs.getLong("order_id"));
            order.setProductArticle(rs.getString("product_article"));
            order.setQuantity(rs.getInt("quantity"));
            order.setTotalAmount(rs.getBigDecimal("total_amount"));
            order.setOrderDate(rs.getObject("order_date", LocalDateTime.class));
            return order;
        }
    }
}
