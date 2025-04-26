package com.fabrikka.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Category {

    @Id
    @Column(name = "category_id")
    UUID categoryId;

    String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }

    @PrePersist
    private void generateUUID() {
        if (categoryId == null) {
            categoryId = UUID.randomUUID();
        }
    }
}
