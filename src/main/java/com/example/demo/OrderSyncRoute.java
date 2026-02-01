package com.example.demo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;


//@Component
public class OrderSyncRoute extends RouteBuilder {

    @Override
    public void configure() {

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
                    order.setOrderDate(((Timestamp) row.get("order_date")));
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


