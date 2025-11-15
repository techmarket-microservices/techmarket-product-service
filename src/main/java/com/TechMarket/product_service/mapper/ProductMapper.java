package com.TechMarket.product_service.mapper;

import com.TechMarket.product_service.dto.request.ProductRequest;
import com.TechMarket.product_service.dto.response.ProductCardResponse;
import com.TechMarket.product_service.dto.response.ProductResponse;
import com.TechMarket.product_service.model.Product;
import com.TechMarket.product_service.model.enums.StatusType;

public class ProductMapper {
    public static Product ProductRequestToProduct(ProductRequest request){
        if(request == null){
            return null;
        }
        Product product = new Product();
        product.setAttributes(request.getAttributes());
        product.setKeyPoints(request.getKeyPoints());
        product.setSlug(request.getSlug());
        product.setPrice(request.getPrice());
        product.setBgGradient(request.getBgGradient());
        product.setStock(request.getStock());
        product.setDiscountPercentage(request.getDiscountPercentage());
        product.setTranslations(request.getTranslations());
        product.setWarranty(request.getWarranty());
        product.setStatus(StatusType.ACTIVE);
        return product;
    }
    public static ProductCardResponse ProductToProductCardResponse(Product product){
        if(product == null){
            return null;
        }
        ProductCardResponse response = new ProductCardResponse();
        response.setProductId(product.getUuid());
        response.setPrice(product.getPrice());
        response.setCoverUrl(product.getCoverUrl());
        response.setSlug(product.getSlug());
        response.setDiscountPercentage(product.getDiscountPercentage());
        response.setStock(product.getStock());
        response.setNew(product.isNew());
        response.setPriceSale(product.getPriceSale());

        return response;
    }
    public static ProductResponse ProductToProductResponse(Product product){
        if(product == null){
            return null;
        }
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getUuid());
        response.setPrice(product.getPrice());
        response.setCoverUrl(product.getCoverUrl());
        response.setSlug(product.getSlug());
        response.setDiscountPercentage(product.getDiscountPercentage());
        response.setStock(product.getStock());
        response.setNew(product.isNew());
        response.setPriceSale(product.getPriceSale());
        response.setWarranty(product.getWarranty());
        response.setImages(product.getImages());
        response.setBgGradient(product.getBgGradient());
        response.setStatus(product.getStatus());
        return response;
    }
}
