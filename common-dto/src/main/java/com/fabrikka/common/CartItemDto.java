package com.fabrikka.common;

import lombok.Data;

import java.util.UUID;

@Data
public class CartItemDto {
    private UUID id;
    private UUID productId;
    private int quantity;
}