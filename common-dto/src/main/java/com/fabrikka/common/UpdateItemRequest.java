package com.fabrikka.common;

import lombok.Data;

@Data
public class UpdateItemRequest {
    private Long itemId;
    private Integer quantity;
}