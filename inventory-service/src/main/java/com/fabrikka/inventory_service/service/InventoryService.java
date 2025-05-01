package com.fabrikka.inventory_service.service;


import com.fabrikka.common.InventoryDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface InventoryService {

    InventoryDto getInventory(UUID productId);

    void updateInventory(UUID productId, Integer quantity);

    void createInventory(InventoryDto inventoryDto);
}
