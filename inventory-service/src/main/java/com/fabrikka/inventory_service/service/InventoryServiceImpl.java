package com.fabrikka.inventory_service.service;

import com.fabrikka.inventory_service.dto.InventoryDto;
import com.fabrikka.inventory_service.entity.Inventory;
import com.fabrikka.inventory_service.repository.InventoryRepository;

import java.util.UUID;

public class InventoryServiceImpl implements InventoryService {

    final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryDto getInventory(UUID productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        if (inventory == null) {
            throw new RuntimeException("Inventory not found for product ID: " + productId);
        }
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setProductId(inventory.getProductId());
        inventoryDto.setAvailableStock(inventory.getAvailableStock());

        return inventoryDto;
    }

    @Override
    public void updateInventory(UUID productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        if (inventory == null) {
            throw new RuntimeException("Inventory not found for product ID: " + productId);
        }
        inventory.setAvailableStock(inventory.getAvailableStock() - quantity);
        inventoryRepository.save(inventory); // Ensure this is saving an Inventory entity
    }
}
