package com.fabrikka.cart_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    private UUID id;

    private UUID productId;
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    @ToString.Exclude
    private Cart cart;

    @PrePersist
    private void generateUUID(){
        if(id == null){
            id = UUID.randomUUID();
        }
    }
}