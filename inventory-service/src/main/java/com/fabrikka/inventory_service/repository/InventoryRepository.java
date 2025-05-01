package com.fabrikka.inventory_service.repository;

import com.fabrikka.inventory_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    Inventory findByProductId(UUID productId);
}
