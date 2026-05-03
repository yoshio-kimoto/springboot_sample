package com.example.inventory.dto;

import com.example.inventory.exception.BusinessException;

public record Quantity (
        Integer qty
){

    public static Quantity from(String str) {
        if (str == null)
            throw new BusinessException("Quantity cannot be null");

        try {
            int qty = Integer.parseInt(str);
            if (qty < 1)
                throw new BusinessException("Quantity should be larger than 1");

            return new Quantity(qty);

        } catch (NumberFormatException ex) {
            throw new BusinessException("Quantity is not parsable: " + str);
        }
    }

}
