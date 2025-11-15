package com.TechMarket.product_service.model;

import com.TechMarket.product_service.model.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity{

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "primary_category_id")
    private Category primaryCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductTranslation> translations = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAttribute> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductKeypoint> keyPoints = new ArrayList<>();

    private BigDecimal price;
    private BigDecimal discountPercentage;
    private int stock;
    private String coverUrl;
    private String slug;
    private Integer warranty;
    private String bgGradient;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private StatusType status;


    @Transient
    public boolean isNew() {
        return createdAt != null &&
                createdAt.isAfter(LocalDateTime.now().minusMonths(2));
    }

    @Transient
    public BigDecimal getPriceSale() {
        if (discountPercentage == null || discountPercentage.compareTo(BigDecimal.ZERO) == 0) {
            return price;
        }

        BigDecimal discount = price.multiply(discountPercentage)
                .divide(new BigDecimal("100"));
        return price.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }

}
