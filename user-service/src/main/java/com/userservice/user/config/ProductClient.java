package com.userservice.user.config;

import com.fabrikka.common.CategoryDto;
import com.fabrikka.common.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "product-service", url = "http://api-gateway:8084")
public interface ProductClient {


    @PostMapping("/products/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductDto productDto);

    @GetMapping("/products/all")
    public ResponseEntity<List<ProductDto>> getAllProducts();

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id);

    @GetMapping("/products/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id);

    @GetMapping("/products/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable UUID id, @RequestBody ProductDto productDto);

    @GetMapping("/products/category")
    public ResponseEntity<List<CategoryDto>> getAllCategory();

    @GetMapping("/products/search")
    ResponseEntity<Page<ProductDto>> getProductsPaginated(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "sort", required = false) String sort);

}
