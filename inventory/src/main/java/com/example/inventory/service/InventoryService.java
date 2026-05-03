package com.example.inventory.service;

import com.example.inventory.dto.InventoryId;
import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.dto.Quantity;
import com.example.inventory.entity.InventoryEntity;
import com.example.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repo;

    @Transactional
    public InventoryResponse decrease(InventoryId id, Quantity qty) {
        InventoryEntity inv = repo.findByIdForUpdate(id.id())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found: " + id.id()));

        inv.decrease(qty);
        InventoryEntity saved = repo.save(inv);

        return toResponse(saved);
    }

    @Transactional
    public InventoryResponse increase(InventoryId id, Quantity qty) {
        InventoryEntity inv = repo.findByIdForUpdate(id.id())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found: " + id.id()));

        inv.increase(qty);
        InventoryEntity saved = repo.save(inv);

        return toResponse(saved);
    }

    @Transactional
    public InventoryResponse reserve(InventoryId id, Quantity qty) {
        InventoryEntity inv = repo.findByIdForUpdate(id.id())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found: " + id.id()));

        inv.reserve(qty);
        InventoryEntity saved = repo.save(inv);

        return toResponse(saved);
    }

    private InventoryResponse toResponse(InventoryEntity entity) {
        return new InventoryResponse(entity.getId(), entity.getStock(), entity.getReserved());
    }

}
