package com.fabrikka.cart_service.controller;


import com.fabrikka.cart_service.service.CartService;
import com.fabrikka.common.AddItemRequest;
import com.fabrikka.common.CartDto;
import com.fabrikka.common.ErrorResponseDto;
import com.fabrikka.common.UpdateItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Tag(name = "Cart Management", description = "Operations related to user carts")
public class CartController {

    private final CartService cartService;

    @PostMapping("/{userId}/add")
    @Operation(summary = "Add item to cart", description = "Adds a product to the user's cart")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item added to cart successfully"),
            @ApiResponse(responseCode = "404", description = "User not found or product not available",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<CartDto> addItem(@PathVariable Long userId, @RequestBody AddItemRequest request) {
        CartDto updatedCart = cartService.addItemToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user's cart", description = "Retrieves the cart for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found for user",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<CartDto> getCart(@PathVariable Long userId) {
        CartDto cartDto = cartService.getCartByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    @PutMapping("/{userId}/update")
    @Operation(summary = "Update item quantity", description = "Updates the quantity of an item in the user's cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item quantity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart or item not found",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<CartDto> updateItem(@PathVariable Long userId, @RequestBody UpdateItemRequest request) {
        CartDto updatedCart = cartService.updateItemQuantity(userId, request.getItemId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(updatedCart);
    }

    @Operation(summary = "Remove item from cart", description = "Removes an item from the user's cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item removed from cart successfully"),
            @ApiResponse(responseCode = "404", description = "Cart or item not found",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{userId}/remove/{itemId}")
    public ResponseEntity<CartDto> removeItem(@PathVariable Long userId, @PathVariable UUID itemId) {
        CartDto updatedCart = cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCart);
    }

    @DeleteMapping("/{userId}/clear")
    @Operation(summary = "Clear cart", description = "Removes all items from the user's cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found for user",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<CartDto> clearCart(@PathVariable Long userId) {
        CartDto updatedCart = cartService.clearCart(userId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCart);
    }
}