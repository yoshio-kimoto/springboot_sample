package com.example.inventory.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

public class InventoryEntity {
    @Id
    @GeneratedValue
    private Long id;

    private int stock;

    @Version
    private Long version;

    // getter/setter
}
