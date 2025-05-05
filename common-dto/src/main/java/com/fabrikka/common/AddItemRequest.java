package com.fabrikka.common;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AddItemRequest {
    private UUID productId;
    private Integer quantity;
}