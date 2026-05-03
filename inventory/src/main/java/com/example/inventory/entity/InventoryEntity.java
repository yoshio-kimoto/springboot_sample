package com.example.inventory.entity;

import com.example.inventory.dto.Quantity;
import com.example.inventory.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    private int stock;

    @Min(1)
    private int reserved;

    public void increase(Quantity quantity) {
        if (quantity.qty() < 1)
            throw new BusinessException("Quantity needs to be larger than 1");
        stock += quantity.qty();
    }

    public void decrease(Quantity quantity) {
        if (quantity.qty() < 1 || stock < quantity.qty())
            throw new BusinessException("There isn't enough stock: quantity: " + quantity.qty() + ", stock: " + stock);
        stock -= quantity.qty();
    }

    public void reserve(Quantity quantity) {
        if (quantity.qty() < 1 || stock < quantity.qty() + reserved)
            throw new BusinessException("There isn't enough stock for reserve: quantity: " + quantity.qty() + ", stock: " + stock);
        reserved += quantity.qty();
    }

    @Version
    private Long version;

}
