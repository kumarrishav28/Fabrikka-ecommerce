package com.fabrikka.inventory_service.controller;

import com.fabrikka.inventory_service.dto.InventoryDto;
import com.fabrikka.inventory_service.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    ResponseEntity<InventoryDto> getInventory(@PathVariable UUID productId) {
        InventoryDto inventory = inventoryService.getInventory(productId);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/{productId}")
    ResponseEntity<String> updateInventory(@PathVariable UUID productId,Integer quantity) {
        inventoryService.updateInventory(productId, quantity);
        return ResponseEntity.ok("Inventory updated successfully");
    }
}
