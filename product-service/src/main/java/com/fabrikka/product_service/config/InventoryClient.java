package com.fabrikka.product_service.config;

import com.fabrikka.common.InventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "inventory-service", url = "http://localhost:8084")
public interface InventoryClient {

    @GetMapping("/inventory/{productId}")
    ResponseEntity<InventoryDto> getInventory(@PathVariable UUID productId);

    @PutMapping("/inventory/{productId}")
    ResponseEntity<String> updateInventory(@PathVariable UUID productId, @RequestParam Integer quantity);

    @PostMapping("/inventory/add")
    ResponseEntity<String> addInventory(@RequestBody InventoryDto inventoryDto);
}