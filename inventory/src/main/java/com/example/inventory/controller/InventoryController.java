package com.example.inventory.controller;

import com.example.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @PostMapping("/{id}/decrease")
    public String decrease(@PathVariable Long id, @RequestParam int qty) {
        return service.decrease(id, qty);
    }

    @PostMapping("/{id}/increase")
    public String increase(@PathVariable Long id, @RequestParam int qty) {
        return service.increase(id, qty);
    }

}
