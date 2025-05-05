package com.fabrikka.common;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
    private UUID id;
    private Long userId;
    private List<CartItemDto> items;
}