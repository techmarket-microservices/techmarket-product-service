package com.TechMarket.product_service.service;

import com.TechMarket.product_service.exceptions.S3FunctionalException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {
    String uploadSingleFile(MultipartFile file) throws S3FunctionalException;
    public List<String> uploadFiles(List<MultipartFile> files) throws S3FunctionalException;
    public void deleteSingleFile(String key) throws S3FunctionalException;
}
