package com.fabrikka.product_service.service;

import com.fabrikka.common.CategoryDto;
import com.fabrikka.common.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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

    public void saveAll(List<ProductDto> productDto);

    Page<ProductDto> getProductsPaginated(int page, int size, List<String> categories, Double minPrice, Double maxPrice, String sort);
}
