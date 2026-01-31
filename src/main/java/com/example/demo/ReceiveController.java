package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReceiveController {

    @PostMapping("/receive")
    public ResponseEntity<String> receive(@RequestBody String body) {
        System.out.println("Reçu : " + body);
        return ResponseEntity.ok("Reçu");
    }
}
