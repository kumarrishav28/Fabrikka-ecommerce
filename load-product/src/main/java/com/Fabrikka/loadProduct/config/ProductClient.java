package com.Fabrikka.loadProduct.config;

import com.fabrikka.common.CategoryDto;
import com.fabrikka.common.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "product-service", url = "http://api-gateway:8084")
public interface ProductClient {


    @PostMapping("/products/addAll")
    public ResponseEntity<String> saveAll(@RequestBody List<ProductDto> productDto);
}
