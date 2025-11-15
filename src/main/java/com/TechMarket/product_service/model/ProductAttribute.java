package com.TechMarket.product_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttribute extends BaseEntity{
    private String nameEn;
    private String nameAr;
    private String valueEn;
    private String valueAr;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
