package com.fabrikka.product_service.service;

import com.fabrikka.product_service.dto.CategoryDto;
import com.fabrikka.product_service.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductService {

    public void createProduct(ProductDto productDto);

    public List<ProductDto> getAllProducts();

    public ProductDto getProductById(UUID id);

    public void deleteProduct(UUID id);

    public void updateProduct(UUID id, ProductDto productDto);

    public List<CategoryDto> getAllCategories();
}
