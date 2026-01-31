package com.example.demo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileToHttpRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("file:input?noop=true")
                .routeId("file-to-http")
                .log("Fichier détecté : ${file:name}")
                .convertBodyTo(String.class)
                .setHeader("Content-Type", constant("text/plain"))
                .to("http://localhost:8080/api/receive")
                .log("Fichier envoyé avec succès");
    }
}
