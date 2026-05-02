package com.example.order.controller;

import com.example.order.dto.OrderId;
import com.example.order.dto.OrderRequest;
import com.example.order.dto.OrderResponse;
import com.example.order.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
//@Validated
public class OrderController {

//    @Autowired
//    private OrderService service;
    private final OrderService service;

    @PostMapping("/{id}/pay")
//    これは意味がない。ValidはPathVariableには無効。
//    public OrderResponse pay(@Valid @PathVariable Long id) {
//    やるなら、@Validatedといっしょにこう。
//    public OrderResponse pay(@Min(1) @PathVariable Long id) {
//        return service.pay(id);
//    }

    public OrderResponse pay(@PathVariable String id) {
        return service.pay(OrderId.from(id));
    }

    @PostMapping("/{id}/ship")
    public OrderResponse ship(@PathVariable String id) {
        return service.ship(OrderId.from(id));
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable String id) {
        return service.cancel(OrderId.from(id));
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
