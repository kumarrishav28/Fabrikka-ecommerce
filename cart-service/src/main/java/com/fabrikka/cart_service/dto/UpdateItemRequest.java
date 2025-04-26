package com.fabrikka.cart_service.dto;

import lombok.Data;

@Data
public class UpdateItemRequest {
    private Long itemId;
    private Integer quantity;
}