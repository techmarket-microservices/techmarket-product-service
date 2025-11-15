package com.TechMarket.product_service.dto.response;

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
public class ProductResponse {
    private String productId;
    private String name;
    private String subDescription;
    private String description;
    private String seoTitle;
    private String seoDescription;
    private List<String> images;
    private List<String> categoryIds;
    private String primaryCategoryId;
    private List<ProductAttributeResponse> attributes;
    private List<ProductKeypointResponse> keyPoints;
    private BigDecimal price;
    private BigDecimal priceSale;
    private BigDecimal discountPercentage;
    private int stock;
    private String coverUrl;
    private String slug;
    private Integer warranty;
    private String bgGradient;
    private boolean isNew;
    private boolean isBestSeller;
    private StatusType status;
}
