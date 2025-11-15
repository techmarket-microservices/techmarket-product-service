package com.TechMarket.product_service.dto.request;

import com.TechMarket.product_service.model.CategoryTranslation;
import com.TechMarket.product_service.model.enums.StatusType;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryRequest {
    private String iconName;
    private String image;
    private String slug;
    private String parentId;
    private List<CategoryTranslation> translations;
    private StatusType status;
}
