package com.example.order.service;

import com.example.order.dto.OrderRequest;
import com.example.order.dto.OrderResponse;
import com.example.order.entity.OrderEntity;
import com.example.order.exception.BusinessException;
import com.example.order.exception.ResourceNotFoundException;
import com.example.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

//    @Autowired
//    private OrderRepository repo;
    private final OrderRepository repo;

    @Transactional
    public OrderResponse pay(Long id) {
//        OrderEntity order = repo.findById(id).get(); // Optional.get()
        OrderEntity order = repo.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order ID not found: " + id));

//        if (order.getStatus().equals("PAID")) {
//            return "already paid"; // 文字列返却
//        }
//        order.setStatus("PAID");

/*
//        This logic should be moved to Entity to make it centralize to judge the status.
        if (order.getStatus().equals(OrderEntity.Status.PAID)) {
            throw new BusinessException("Order ID " + id + " is already PAID");
        }
        order.setStatus(OrderEntity.Status.PAID);
*/
        order.pay();

        OrderEntity saved = repo.save(order); // save() の戻り値無視

//        return "ok";
        return toResponse(saved);
    }

    @Transactional
    public OrderResponse ship(Long id) {
//        OrderEntity order = repo.findById(id).orElse(null); // null 返却
        OrderEntity order = repo.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order ID not found: " + id));

//        if (!order.getStatus().equals("PAID")) {
//            return "cannot ship"; // ビジネスロジックが文字列
//        }

/*
        if (!order.getStatus().equals(OrderEntity.Status.PAID)) {
            throw new BusinessException("Order ID " + id + " cannot ship: " + order.getStatus());
        }
        order.setStatus(OrderEntity.Status.SHIPPED);
*/
        order.ship();

        OrderEntity saved = repo.save(order);

//        return "ok";
        return toResponse(saved);

    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        OrderEntity entity = OrderEntity.builder()
                .status(OrderEntity.Status.CREATED)
                .memo(request.memo())
                .build();

        OrderEntity saved = repo.save(entity);
        return toResponse(saved);
    }

    public List<OrderResponse> findAll() {
        return repo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(OrderEntity entity) {
        return new OrderResponse(entity.getId(), entity.getStatus());
    }

}
