package com.fabrikka.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Product {

    @Id
    UUID productId;

    String name;

    String description;

    BigDecimal price;

    String imageUrl;

    Integer stock;

    UUID categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


   @PrePersist
   public void generateUUID(){
       if(productId == null){
              productId = UUID.randomUUID();
       }
   }

}
