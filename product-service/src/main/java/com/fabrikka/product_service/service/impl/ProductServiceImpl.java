package com.fabrikka.product_service.service.impl;

import com.fabrikka.product_service.dto.CategoryDto;
import com.fabrikka.product_service.dto.ProductDto;
import com.fabrikka.product_service.entity.Category;
import com.fabrikka.product_service.entity.Product;
import com.fabrikka.product_service.repository.CategoryRepository;
import com.fabrikka.product_service.repository.ProductRepository;
import com.fabrikka.product_service.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;

    final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


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
        product.setStock(productDto.getStock());
        productRepository.save(product);

    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> new ProductDto(
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getImageUrl(),
                        product.getStock(),
                        new CategoryDto(product.getName())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductDto(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getStock(),
                new CategoryDto(product.getName())
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
        product.setStock(productDto.getStock());
        Category category = new Category();
        category.setName(productDto.getCategory().getName());
        product.setCategory(category);
        productRepository.save(product);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryDto(category.getName()))
                .collect(Collectors.toList());
    }


}
