package com.example.inventory.controller;

import com.example.inventory.dto.InventoryId;
import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.dto.Quantity;
import com.example.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @PostMapping("/{id}/decrease")
    public InventoryResponse decrease(@PathVariable String id, @Valid @RequestBody String qty) {
        return service.decrease(InventoryId.from(id), Quantity.from(qty));
    }

    @PostMapping("/{id}/increase")
    public InventoryResponse increase(@PathVariable String id, @Valid @RequestBody String qty) {
        return service.increase(InventoryId.from(id), Quantity.from(qty));
    }

    @PostMapping("/{id}/reserve")
    public InventoryResponse reserve(@PathVariable String id, @Valid @RequestBody String qty) {
        return service.reserve(InventoryId.from(id), Quantity.from(qty));
    }

}
