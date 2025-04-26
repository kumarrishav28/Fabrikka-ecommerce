package com.fabrikka.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDto {

    String name;

    String description;

    BigDecimal price;

    String imageUrl;

    Integer stock;

    CategoryDto category;
}
