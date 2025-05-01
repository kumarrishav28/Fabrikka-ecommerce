package com.fabrikka.inventory_service.service;


import com.fabrikka.common.InventoryDto;
import com.fabrikka.inventory_service.entity.Inventory;
import com.fabrikka.inventory_service.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
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

    @Override
    public void createInventory(InventoryDto inventoryDto) {
        Inventory inventory = new Inventory();
        inventory.setProductId(inventoryDto.getProductId());
        inventory.setAvailableStock(inventoryDto.getAvailableStock());
        inventoryRepository.save(inventory);
    }
}
