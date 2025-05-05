package com.fabrikka.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDto {

    String name;

    String description;

    BigDecimal price;

    String imageUrl;

    CategoryDto category;

    InventoryDto inventory;

    UUID productId;
}
