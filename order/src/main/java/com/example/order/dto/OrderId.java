package com.example.order.dto;

import com.example.order.exception.BusinessException;

public record OrderId(
        Long value
) {

    public static OrderId from(String raw) {
        try {
            return new OrderId(Long.parseLong(raw));
        } catch (NumberFormatException ex) {
            throw new BusinessException("Number string " + raw + "is not numeric.");
        }
    }
}
