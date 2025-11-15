package com.TechMarket.product_service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductCardResponse {
    private String productId;
    private String name;
    private BigDecimal price;
    private BigDecimal priceSale;
    private BigDecimal discountPercentage;
    private int stock;
    private String coverUrl;
    private String slug;
    private boolean isNew;
}