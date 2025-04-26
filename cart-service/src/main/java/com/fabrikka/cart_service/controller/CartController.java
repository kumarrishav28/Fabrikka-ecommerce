package com.fabrikka.cart_service.controller;


import com.fabrikka.cart_service.dto.AddItemRequest;
import com.fabrikka.cart_service.dto.UpdateItemRequest;
import com.fabrikka.cart_service.entity.Cart;
import com.fabrikka.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addItem(@PathVariable Long userId, @RequestBody AddItemRequest request) {
        Cart updatedCart = cartService.addItemToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<Cart> updateItem(@PathVariable Long userId, @RequestBody UpdateItemRequest request) {
        Cart updatedCart = cartService.updateItemQuantity(userId, request.getItemId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/remove/{itemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable Long userId, @PathVariable Long itemId) {
        Cart updatedCart = cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Cart> clearCart(@PathVariable Long userId) {
        Cart updatedCart = cartService.clearCart(userId);
        return ResponseEntity.ok(updatedCart);
    }
}