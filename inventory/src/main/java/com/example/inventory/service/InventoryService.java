package com.example.inventory.service;

import com.example.inventory.entity.InventoryEntity;
import com.example.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository repo;

    @Transactional
    public String decrease(Long id, int qty) {
        InventoryEntity inv = repo.findById(id).orElse(null);

        if (inv.getStock() < qty) {
            return "not enough stock";
        }

        inv.setStock(inv.getStock() - qty);
        repo.save(inv);

        return "ok";
    }

    @Transactional
    public String increase(Long id, int qty) {
        InventoryEntity inv = repo.findById(id).get();
        inv.setStock(inv.getStock() + qty);
        repo.save(inv);
        return "ok";
    }
}
