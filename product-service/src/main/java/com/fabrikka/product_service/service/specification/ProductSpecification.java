package com.fabrikka.product_service.service.specification;

import com.fabrikka.product_service.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class provides reusable Specification predicates for querying the Product entity.
 * Specifications are used to build dynamic, type-safe queries.
 */
@Component
public class ProductSpecification {

    public static Specification<Product> hasCategoryIn(List<String> categories) {
        return (root, query, criteriaBuilder) ->
                root.get("category").get("name").in(categories);
    }

    public static Specification<Product> isGreaterThanOrEqualToPrice(Double minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> isLessThanOrEqualToPrice(Double maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}