package com.Fabrikka.loadProduct.service;

import com.fabrikka.common.ProductDto;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductItemProcessor implements ItemProcessor<ProductDto, ProductDto> {

    @Override
    public ProductDto process(ProductDto productDto) throws Exception {
        // Here you can add any processing logic for the product
        // For example, you might want to validate or transform the product data
        if (productDto.getPrice() != null && productDto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        // Return the processed product
        return productDto;
    }
}
