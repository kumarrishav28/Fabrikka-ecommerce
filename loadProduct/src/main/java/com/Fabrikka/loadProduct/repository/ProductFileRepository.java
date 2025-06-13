package com.Fabrikka.loadProduct.repository;

import com.Fabrikka.loadProduct.entity.ProductFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFile,Long> {
}
