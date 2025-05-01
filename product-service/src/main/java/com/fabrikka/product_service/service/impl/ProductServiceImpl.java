package com.fabrikka.product_service.service.impl;

import com.fabrikka.common.CategoryDto;
import com.fabrikka.common.InventoryDto;
import com.fabrikka.common.ProductDto;
import com.fabrikka.product_service.config.InventoryClient;
import com.fabrikka.product_service.entity.Category;
import com.fabrikka.product_service.entity.Product;
import com.fabrikka.product_service.repository.CategoryRepository;
import com.fabrikka.product_service.repository.ProductRepository;
import com.fabrikka.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;

    final CategoryRepository categoryRepository;

    final InventoryClient inventoryClient;


    @Override
    public void createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        Category category = new Category();
        category.setName(productDto.getCategory().getName());
        product.setCategory(category);
        product.setImageUrl(productDto.getImageUrl());
        product = productRepository.save(product);
        InventoryDto inventory =  productDto.getInventory();
        inventory.setProductId(product.getProductId());
        createInventory(inventory);

    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> {
                    InventoryDto inventory = inventoryClient.getInventory(product.getProductId()).getBody();
                    return new ProductDto(
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getImageUrl(),
                            new CategoryDto(product.getCategory().getName()),
                            inventory
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        InventoryDto inventory = inventoryClient.getInventory(product.getProductId()).getBody();
        return new ProductDto(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                new CategoryDto(product.getCategory().getName()),
                inventory
        );
    }

    @Override
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public void updateProduct(UUID id, ProductDto productDto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        Category category = new Category();
        category.setName(productDto.getCategory().getName());
        product.setCategory(category);
        productRepository.save(product);
        inventoryClient.updateInventory(product.getProductId(), productDto.getInventory().getAvailableStock());
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryDto(category.getName()))
                .collect(Collectors.toList());
    }

    public void createInventory(InventoryDto inventoryDto) {
        inventoryClient.addInventory(inventoryDto);
    }

}
