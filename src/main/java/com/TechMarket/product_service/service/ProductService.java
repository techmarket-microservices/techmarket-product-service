package com.TechMarket.product_service.service;

import com.TechMarket.product_service.dto.request.ProductRequest;
import com.TechMarket.product_service.dto.response.ProductResponse;
import com.TechMarket.product_service.model.Product;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductRequest productRequest, MultipartFile cover, List<MultipartFile> images) throws Exception;
    Product updateProduct(String id, ProductRequest productRequest, MultipartFile cover, List<MultipartFile> images) throws Exception;
    @Transactional(readOnly = true)
    ProductResponse getProductById(String id) throws Exception;
    List<ProductResponse> getProductsByCategory(int page, int size,String categoryId, double priceMax, double priceMin, double discount, String search) throws Exception;
    void softDeleteProduct(String id) throws Exception;
}
