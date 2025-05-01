package com.fabrikka.common;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddItemRequest {
    private Long productId;
    private Integer quantity;
}