package com.fabrikka.cart_service.controller;


import com.fabrikka.cart_service.service.CartService;
import com.fabrikka.common.AddItemRequest;
import com.fabrikka.common.CartDto;
import com.fabrikka.common.UpdateItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartDto> addItem(@PathVariable Long userId, @RequestBody AddItemRequest request) {
        CartDto updatedCart = cartService.addItemToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<CartDto> updateItem(@PathVariable Long userId, @RequestBody UpdateItemRequest request) {
        CartDto updatedCart = cartService.updateItemQuantity(userId, request.getItemId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/remove/{itemId}")
    public ResponseEntity<CartDto> removeItem(@PathVariable Long userId, @PathVariable Long itemId) {
        CartDto updatedCart = cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<CartDto> clearCart(@PathVariable Long userId) {
        CartDto updatedCart = cartService.clearCart(userId);
        return ResponseEntity.ok(updatedCart);
    }
}