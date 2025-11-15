package com.TechMarket.product_service.dto.request;

import com.TechMarket.product_service.model.Category;
import com.TechMarket.product_service.model.ProductAttribute;
import com.TechMarket.product_service.model.ProductKeypoint;
import com.TechMarket.product_service.model.ProductTranslation;
import com.TechMarket.product_service.model.enums.StatusType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductRequest {
    private List<String> images;
    private List<String> categories;
    private String primaryCategory;
    private List<ProductTranslation> translations;
    private List<ProductAttribute> attributes;
    private List<ProductKeypoint> keyPoints;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private int stock;
    private String coverUrl;
    private String slug;
    private Integer warranty;
    private String bgGradient;
    private StatusType status;
}
