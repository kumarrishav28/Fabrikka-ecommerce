package com.Fabrikka.loadProduct.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ProductFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id ;
    private String fileName;
    private LocalDateTime uploadedAt;
    private String status ;
    @Lob
    private byte[] fileData;


}
