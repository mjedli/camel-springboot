package com.example.demo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Order order) {
        String sql = "INSERT INTO orders_end (order_number, customer_name, order_date, status, total_amount, currency, delivery_address) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCurrency(),
                order.getDeliveryAddress());
    }
}
