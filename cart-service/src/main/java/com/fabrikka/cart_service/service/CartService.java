package com.fabrikka.cart_service.service;

import com.fabrikka.cart_service.entity.Cart;
import com.fabrikka.cart_service.entity.CartItem;
import com.fabrikka.cart_service.repository.CartItemRepository;
import com.fabrikka.cart_service.repository.CartRepository;
import com.fabrikka.common.CartDto;
import com.fabrikka.common.CartItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    public CartDto createCart(Long userId) {
        Cart cart = Cart.builder().userId(userId).build();
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDto(savedCart);
    }

    public Optional<CartDto> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).map(cartMapper::toDto);
    }

    public CartDto addItemToCart(Long userId, UUID productId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> cartMapper.toEntity(createCart(userId)));

        CartItem item = CartItem.builder()
                .productId(productId)
                .quantity(quantity)
                .cart(cart)
                .build();

        cart.getItems().add(item);
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toDto(updatedCart);
    }

    public CartDto updateItemQuantity(Long userId, Long itemId, Integer newQuantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems()
                .stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setQuantity(newQuantity);
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toDto(updatedCart);
    }

    public CartDto removeItemFromCart(Long userId, Long itemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toDto(updatedCart);
    }

    public CartDto clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toDto(updatedCart);
    }
}