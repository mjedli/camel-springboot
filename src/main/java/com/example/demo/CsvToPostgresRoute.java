package com.example.demo;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class CsvToPostgresRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("file:input?fileName=orders.csv&noop=true")
                .routeId("csv-to-postgres")
                .unmarshal().csv()
                .split(body()).streaming()
                .filter(simple("${body[0]} != 'order_number'")) // ignore header
                .process(exchange -> {
                    List<String> row = exchange.getIn().getBody(List.class);
                    Map<String, Object> data = new HashMap<>();
                    data.put("order_number", row.get(0));
                    data.put("customer_name", row.get(1));
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                    LocalDateTime localDateTime = LocalDateTime.parse(row.get(2), formatter);
                    data.put("order_date", Timestamp.valueOf(localDateTime));
                    data.put("status", row.get(3));
                    data.put("total_amount", new BigDecimal(row.get(4)));
                    data.put("currency", row.get(5));
                    data.put("delivery_address", row.get(6));
                    exchange.getIn().setBody(data);
                })
                .to("sql:INSERT INTO orders (order_number, customer_name, order_date, status, total_amount, currency, delivery_address) " +
                        "VALUES (:#order_number, :#customer_name, :#order_date, :#status, :#total_amount, :#currency, :#delivery_address)?dataSource=#dataSource")
                .log("✅ Commande insérée : ${body}");

        from("timer:order-poller?period=60000")
                .routeId("order-sync-route")
                .log("🔄 Vérification des nouvelles commandes dans l'ERP...")
                .to("sql:select * from orders where status = 'NEW'?dataSource=#dataSource")
                .split(body())
                .process(exchange -> {
                    Map<String, Object> row = exchange.getIn().getBody(Map.class);

                    Order order = new Order();
                    order.setId(((Number) row.get("id")).longValue());
                    order.setOrderNumber((String) row.get("order_number"));
                    order.setCustomerName((String) row.get("customer_name"));
                    order.setOrderDate((Timestamp) row.get("order_date"));
                    order.setStatus((String) row.get("status"));
                    order.setTotalAmount((BigDecimal) row.get("total_amount"));
                    order.setCurrency((String) row.get("currency"));
                    order.setDeliveryAddress((String) row.get("delivery_address"));
                    order.setLastUpdated(((Timestamp) row.get("last_updated")));

                    exchange.getIn().setBody(order);
                })
                .marshal().json() // facultatif si tu veux envoyer en JSON
                .log("📦 Envoi de la commande : ${body}")
                .setHeader("Content-Type", constant("application/json"))
                .to("http://localhost:8080/api/insert")
                .log("✅ Commande envoyée au CRM");
    }
}


