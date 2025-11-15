package com.TechMarket.product_service.service;

import com.TechMarket.product_service.dto.request.CategoryRequest;
import com.TechMarket.product_service.dto.response.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryRequest, MultipartFile image) throws Exception;
    CategoryResponse updateCategory(String id, CategoryRequest categoryRequest, MultipartFile image) throws Exception;
    List<CategoryResponse> getCategories();
    void softDelete(String id) throws Exception;
}
