package com.TechMarket.product_service.dto.response;

import com.TechMarket.product_service.model.enums.StatusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    private String categoryId;
    private String name;
    private String seoTitle;
    private String seoDescription;
    private String iconName;
    private String image;
    private String slug;
    private String parentId;
    private StatusType status;
}
