package com.example.demo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


//@Component
public class OrderSyncRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("timer:order-poller?period=60000") // toutes les 60s
                .routeId("order-sync-route")
                .log("🔄 Vérification des nouvelles commandes dans l'ERP...")
                .to("sql:select * from orders where status = 'NEW'?dataSource=#dataSource")
                .split(body())
                .marshal().json()
                .log("📦 Envoi de la commande : ${body}")
                .setHeader("Content-Type", constant("application/json"))
                .to("http://crm.example.com/api/orders")
                .log("✅ Commande envoyée au CRM")
                .end();
    }
}

