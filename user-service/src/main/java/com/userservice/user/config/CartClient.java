package com.userservice.user.config;

import com.fabrikka.common.AddItemRequest;
import com.fabrikka.common.CartDto;
import com.fabrikka.common.UpdateItemRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "cart-service", url = "http://localhost:8084")
public interface CartClient {

    @PostMapping("/carts/{userId}/add")
    ResponseEntity<CartDto> addItemToCart(@PathVariable Long userId, @RequestBody AddItemRequest request);

    @GetMapping("/carts/{userId}")
    ResponseEntity<CartDto> getCart(@PathVariable Long userId);

    @PutMapping("/carts/{userId}/update")
    ResponseEntity<CartDto> updateItemQuantity(@PathVariable Long userId, @RequestBody UpdateItemRequest request);

    @DeleteMapping("/carts/{userId}/remove/{itemId}")
    ResponseEntity<CartDto> removeItemFromCart(@PathVariable Long userId, @PathVariable UUID itemId);

    @DeleteMapping("/carts/{userId}/clear")
    ResponseEntity<CartDto> clearCart(@PathVariable Long userId);


}
