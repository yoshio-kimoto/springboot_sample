package com.example.order.dto;

import com.example.order.entity.OrderEntity;

public record OrderResponse(
   Long id,
   OrderEntity.Status status
) {}
