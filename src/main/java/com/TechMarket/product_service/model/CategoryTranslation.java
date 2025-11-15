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
public class CategoryTranslation extends BaseEntity{
    private String locale;
    private String name;
    private String seoTitle;
    private String seoDescription;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
