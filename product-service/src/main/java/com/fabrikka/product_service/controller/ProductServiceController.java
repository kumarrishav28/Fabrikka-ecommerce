package com.fabrikka.product_service.controller;

import com.fabrikka.common.CategoryDto;
import com.fabrikka.common.ProductDto;
import com.fabrikka.product_service.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductServiceController {

    final ProductService productService;

    public ProductServiceController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
        return new ResponseEntity<>("Product added", HttpStatus.CREATED);
    }


    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        ProductDto product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product deleted", HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable UUID id, @RequestBody ProductDto productDto) {
        productService.updateProduct(id, productDto);
        return new ResponseEntity<>("Product updated", HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<CategoryDto> products = productService.getAllCategories();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/addAll")
    public ResponseEntity<String> saveAll(@RequestBody List<ProductDto> productDto) {
        productService.saveAll(productDto);
        return new ResponseEntity<>("Products added", HttpStatus.CREATED);
    }

    @GetMapping("/search")
    ResponseEntity<Page<ProductDto>> getProductsPaginated(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "sort", required = false) String sort){

        return new ResponseEntity<>(productService.getProductsPaginated(page, size, categories, minPrice, maxPrice, sort),HttpStatus.OK);
    }


}
