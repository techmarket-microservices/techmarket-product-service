package com.TechMarket.product_service.mapper;

import com.TechMarket.product_service.dto.request.CategoryRequest;
import com.TechMarket.product_service.dto.response.CategoryResponse;
import com.TechMarket.product_service.model.Category;
import com.TechMarket.product_service.model.enums.StatusType;

public class CategoryMapper {
    public static Category CategoryRequestToCategory(CategoryRequest request){
        if(request == null){
            return null;
        }
        Category category = new Category();
        category.setSlug(request.getSlug());
        category.setImage(request.getImage());
        category.setIconName(request.getIconName());
        category.setTranslations(request.getTranslations());
        category.setStatus(StatusType.ACTIVE);
        return category;
    }
    public static CategoryResponse CategoryToCategoryResponse(Category category){
        if(category == null){
            return null;
        }
        CategoryResponse response = new CategoryResponse();
        response.setCategoryId(category.getUuid());
        response.setImage(category.getImage());
        response.setSlug(category.getSlug());
        response.setIconName(category.getIconName());
        response.setStatus(category.getStatus());
        if (category.getParentId() != null) {
            response.setParentId(category.getParentId().getUuid());
        }
        return response;
    }
}
