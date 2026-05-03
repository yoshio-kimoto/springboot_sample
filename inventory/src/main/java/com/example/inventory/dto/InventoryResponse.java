package com.example.inventory.dto;

public record InventoryResponse(
   Long id,
   int stock,
   int reserved
) {}
