package com.example.order.controller;

import com.example.order.dto.OrderRequest;
import com.example.order.dto.OrderResponse;
import com.example.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

//    @Autowired
//    private OrderService service;
    private final OrderService service;

    @PostMapping("/{id}/pay")
    public OrderResponse pay(@Valid @PathVariable Long id) {
        return service.pay(id);
    }

    @PostMapping("/{id}/ship")
    public OrderResponse ship(@Valid @PathVariable Long id) {
        return service.ship(id);
    }

    @PostMapping()
    public OrderResponse create(@Valid @RequestBody OrderRequest request) {
        return service.create(request);
    }

    @GetMapping()
    public List<OrderResponse> findAll() {
        return service.findAll();
    }
}
