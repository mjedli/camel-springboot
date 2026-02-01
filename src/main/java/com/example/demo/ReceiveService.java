package com.example.demo;

import jakarta.transaction.Transactional;
import org.apache.camel.spi.AsEndpointUri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@Transactional
public class ReceiveService {

    private final OrderRepository orderRepository;
    public ReceiveService(OrderRepository orderRepository)
    { this.orderRepository = orderRepository; }
    private void insert(Order insertdata) {
        orderRepository.save(insertdata);
    }
}
