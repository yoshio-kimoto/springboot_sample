package com.example.inventory.dto;

import com.example.inventory.exception.BusinessException;

public record InventoryId(
        Long id
) {

    public static InventoryId from(String str) {
        if (str == null)
            throw new BusinessException("Quantity cannot be null");

        try {
            return new InventoryId(Long.parseLong(str));
        } catch (NumberFormatException ex) {
            throw new BusinessException("Inventory ID " + str + " is not parsable for Long");
        }
    }

}
