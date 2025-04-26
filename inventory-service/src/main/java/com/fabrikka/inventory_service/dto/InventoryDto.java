package com.fabrikka.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InventoryDto {

    private UUID inventoryId;

    private UUID productId;

    private Integer availableStock;

}
