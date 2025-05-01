package com.fabrikka.cart_service.service;

import com.fabrikka.cart_service.entity.Cart;
import com.fabrikka.cart_service.entity.CartItem;
import com.fabrikka.common.CartDto;
import com.fabrikka.common.CartItemDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartDto toDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUserId());
        cartDto.setItems(cart.getItems().stream().map(this::toItemDto).collect(Collectors.toList()));
        return cartDto;
    }

    private CartItemDto toItemDto(CartItem item) {
        CartItemDto itemDto = new CartItemDto();
        itemDto.setId(item.getId());
        itemDto.setProductId(item.getProductId());
        itemDto.setQuantity(item.getQuantity());
        return itemDto;
    }

    public Cart toEntity(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setId(cartDto.getId());
        cart.setUserId(cartDto.getUserId());
        cart.setItems(cartDto.getItems().stream().map(this::toItemEntity).collect(Collectors.toList()));
        return cart;
    }

    private CartItem toItemEntity(CartItemDto itemDto) {
        CartItem item = new CartItem();
        item.setId(itemDto.getId());
        item.setProductId(itemDto.getProductId());
        item.setQuantity(itemDto.getQuantity());
        return item;
    }
}