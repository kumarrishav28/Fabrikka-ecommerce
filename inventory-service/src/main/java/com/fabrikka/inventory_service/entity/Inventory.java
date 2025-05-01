package com.fabrikka.inventory_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Inventory {

    @Id
    UUID inventoryId;

    UUID productId;

    Integer availableStock;


    @PrePersist
    public void getInventoryId() {
        if (inventoryId == null) {
            inventoryId = UUID.randomUUID();
        }
    }
}
