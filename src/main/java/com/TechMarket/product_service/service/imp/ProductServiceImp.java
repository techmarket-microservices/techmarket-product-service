package com.TechMarket.product_service.service.imp;

import com.TechMarket.product_service.constants.MessageConstants;
import com.TechMarket.product_service.dto.request.ProductRequest;
import com.TechMarket.product_service.dto.response.ProductResponse;
import com.TechMarket.product_service.mapper.ProductMapper;
import com.TechMarket.product_service.model.Category;
import com.TechMarket.product_service.model.Product;
import com.TechMarket.product_service.repository.CategoryRepository;
import com.TechMarket.product_service.repository.ProductRepository;
import com.TechMarket.product_service.service.ProductService;
import com.TechMarket.product_service.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;

    @Override
    public Product createProduct(ProductRequest productRequest, MultipartFile cover, List<MultipartFile> images) throws Exception {
        if (productRequest.getPrimaryCategory() == null || productRequest.getPrimaryCategory().isEmpty()){
            throw new RuntimeException(MessageConstants.CATEGORY_EMPTY);
        }
        Product product = ProductMapper.ProductRequestToProduct(productRequest);

        Category primaryCategory = categoryRepository.findById(productRequest.getPrimaryCategory())
                .orElseThrow(() -> new RuntimeException(MessageConstants.CATEGORY_NOT_FOUND));
        product.setPrimaryCategory(primaryCategory);

        List<Category> categories = new ArrayList<>();
        if (productRequest.getCategories() != null) {
            for (String categoryId : productRequest.getCategories()) {
                if (!categoryId.equals(productRequest.getPrimaryCategory())) {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category with ID " + categoryId + " not found"));
                    categories.add(category);
                }
            }
        }
        product.setCategories(categories);

        if (cover != null && !cover.isEmpty()) {
            String coverKey = s3Service.uploadSingleFile(cover);
            product.setCoverUrl(coverKey);
        }

        if (images != null && !images.isEmpty()) {
            List<String> imageKeys = s3Service.uploadFiles(images);
            product.setImages(imageKeys);
        }

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String id, ProductRequest productRequest, MultipartFile cover, List<MultipartFile> images) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageConstants.PRODUCT_NOT_FOUND));
        product.setWarranty(productRequest.getWarranty());
        product.setTranslations(productRequest.getTranslations());
        product.setStock(productRequest.getStock());
        product.setSlug(productRequest.getSlug());
        product.setAttributes(productRequest.getAttributes());
        product.setKeyPoints(productRequest.getKeyPoints());
        product.setBgGradient(productRequest.getBgGradient());
        product.setPrice(productRequest.getPrice());
        product.setDiscountPercentage(productRequest.getDiscountPercentage());

        if (productRequest.getPrimaryCategory() != null && !productRequest.getPrimaryCategory().isEmpty()) {
            Category primaryCategory = categoryRepository.findById(productRequest.getPrimaryCategory())
                    .orElseThrow(() -> new RuntimeException(MessageConstants.CATEGORY_NOT_FOUND));
            product.setPrimaryCategory(primaryCategory);
        }

        List<Category> categories = new ArrayList<>();
        if (productRequest.getCategories() != null) {
            for (String categoryId : productRequest.getCategories()) {
                if (!categoryId.equals(productRequest.getPrimaryCategory())) {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category with ID " + categoryId + " not found"));
                    categories.add(category);
                }
            }
        }
        product.setCategories(categories);

        String coverKey = product.getCoverUrl();
        if (cover != null && !cover.isEmpty()) {
            if (coverKey != null && !coverKey.isEmpty()) {
                s3Service.deleteSingleFile(coverKey);
            }
            coverKey = s3Service.uploadSingleFile(cover);
        }
        product.setCoverUrl(coverKey);

        List<String> currentImageKeys = product.getImages() != null ? new ArrayList<>(product.getImages()) : new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (String oldKey : currentImageKeys) {
                s3Service.deleteSingleFile(oldKey);
            }
            List<String> newKeys = s3Service.uploadFiles(images);
            product.setImages(newKeys);
        }

        productRepository.save(product);
        return product;
    }

    @Override
    public ProductResponse getProductById(String id) throws Exception {
        return null;
    }

    @Override
    public List<ProductResponse> getProductsByCategory(int page, int size, String categoryId, double priceMax, double priceMin, double discount, String search) throws Exception {
        return List.of();
    }

    @Override
    public void softDeleteProduct(String id) throws Exception {

    }
}
