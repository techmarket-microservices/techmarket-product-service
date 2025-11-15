package com.TechMarket.product_service.service.imp;

import com.TechMarket.product_service.constants.MessageConstants;
import com.TechMarket.product_service.dto.request.CategoryRequest;
import com.TechMarket.product_service.dto.response.CategoryResponse;
import com.TechMarket.product_service.mapper.CategoryMapper;
import com.TechMarket.product_service.model.Category;
import com.TechMarket.product_service.model.enums.StatusType;
import com.TechMarket.product_service.repository.CategoryRepository;
import com.TechMarket.product_service.repository.ProductRepository;
import com.TechMarket.product_service.service.CategoryService;
import com.TechMarket.product_service.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;
    private final ProductRepository productRepository;
    @Autowired
    public CategoryServiceImp(CategoryRepository categoryRepository, S3Service s3Service, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.s3Service = s3Service;
        this.productRepository = productRepository;
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest, MultipartFile image)  throws Exception{
        Category category = CategoryMapper.CategoryRequestToCategory(categoryRequest);

        if(categoryRequest.getParentId() != null){
            Category parentCategory = categoryRepository.findById(categoryRequest.getParentId())
                    .orElseThrow(() -> new RuntimeException(MessageConstants.CATEGORY_NOT_FOUND));
            category.setParentId(parentCategory);
        }
        if (image != null && !image.isEmpty()) {
            String imageKey = s3Service.uploadSingleFile(image);
            category.setImage(imageKey);
        }
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.CategoryToCategoryResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(String id, CategoryRequest categoryRequest, MultipartFile image)  throws Exception{
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException(MessageConstants.CATEGORY_NOT_FOUND));

        category.setSlug(categoryRequest.getSlug());
        category.setTranslations(categoryRequest.getTranslations());
        category.setIconName(categoryRequest.getIconName());
        if (categoryRequest.getParentId() != null){
            if(!categoryRequest.getParentId().equals(category.getParentId() != null ? category.getParentId().getUuid() : null)){
                Category parentCategory = categoryRepository.findById(categoryRequest.getParentId())
                        .orElseThrow(() -> new RuntimeException(MessageConstants.CATEGORY_NOT_FOUND));
                category.setParentId(parentCategory);
            }
        }else{
            category.setParentId(null);
        }

        if (image != null && !image.isEmpty()) {
            String oldImageKey = category.getImage();
            if (oldImageKey != null && !oldImageKey.isEmpty()) {
                s3Service.deleteSingleFile(oldImageKey);
            }
            String newImageKey = s3Service.uploadSingleFile(image);
            category.setImage(newImageKey);
        }

        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.CategoryToCategoryResponse(updatedCategory);
    }

    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::CategoryToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void softDelete(String id) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageConstants.CATEGORY_NOT_FOUND));

        boolean isPrimary = productRepository.existsByPrimaryCategory(category);
        boolean hasProducts = productRepository.existsByCategoriesContaining(category);
        if (hasProducts) {
            throw new RuntimeException("Cannot delete category because it has associated products");
        }

        if (category.getImage() != null && !category.getImage().isEmpty()){
            s3Service.deleteSingleFile(category.getImage());
        }
        category.setStatus(StatusType.DELETED);
        categoryRepository.save(category);
    }
}
