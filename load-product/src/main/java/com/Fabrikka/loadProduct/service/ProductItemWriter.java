package com.Fabrikka.loadProduct.service;

import com.Fabrikka.loadProduct.config.ProductClient;
import com.fabrikka.common.ProductDto;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ProductItemWriter implements ItemWriter<ProductDto> {

    private final ProductClient productClient;

    @Override
    public void write(Chunk<? extends ProductDto> chunk) throws Exception {
        if (chunk.isEmpty()) {
            return;
        }
        List<? extends ProductDto> productDtoList = chunk.getItems();
        String message = productClient.saveAll(new ArrayList<>(productDtoList)).getBody();
    }
}
