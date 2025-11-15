package com.TechMarket.product_service.service.imp;

import com.TechMarket.product_service.config.props.AWSConfigProperties;
import com.TechMarket.product_service.exceptions.S3FunctionalException;
import com.TechMarket.product_service.exceptions.enums.S3FunctionalExceptionType;
import com.TechMarket.product_service.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3ServiceImp implements S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final AWSConfigProperties awsConfigProperties;

    public S3ServiceImp(S3Client s3Client, S3Presigner s3Presigner, AWSConfigProperties awsConfigProperties) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.awsConfigProperties = awsConfigProperties;
    }

    @Override
    public String uploadSingleFile(MultipartFile file) throws S3FunctionalException {
        if (!bucketExists()){
            throw new S3FunctionalException(S3FunctionalExceptionType.BUCKET_NOT_FOUND);
        }
        try {
            isValidFile(file);

            String fileName = file.getOriginalFilename();
            String key = System.currentTimeMillis() + "_" + fileName;
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(awsConfigProperties.getBucketName())
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
            return key;
        } catch (IOException e) {
            throw new S3FunctionalException(S3FunctionalExceptionType.FILE_UPLOAD_ERROR, e.getMessage());
        }
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) throws S3FunctionalException {
        if (!bucketExists()){
            throw new S3FunctionalException(S3FunctionalExceptionType.BUCKET_NOT_FOUND);
        }
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                isValidFile(file);

                String fileName = file.getOriginalFilename();
                String key = System.currentTimeMillis() + "_" + fileName;

                // Upload vers S3
                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(awsConfigProperties.getBucketName())
                                .key(key)
                                .contentType(file.getContentType())
                                .build(),
                        RequestBody.fromBytes(file.getBytes())
                );

                fileUrls.add(key);

            } catch (IOException e) {
                throw new S3FunctionalException(S3FunctionalExceptionType.FILE_UPLOAD_ERROR, e.getMessage());
            }
        }

        return fileUrls;
    }

    @Override
    public void deleteSingleFile(String key) throws S3FunctionalException {
        try {
            if (!fileExists(awsConfigProperties.getBucketName(), key)) {
                throw new S3FunctionalException(S3FunctionalExceptionType.OBJECT_NOT_FOUND);
            }
            s3Client.deleteObject(builder -> builder
                    .bucket(awsConfigProperties.getBucketName())
                    .key(key)
            );
        } catch (S3Exception e) {
            throw new S3FunctionalException(
                    S3FunctionalExceptionType.S3_ACCESS_ERROR,
                    "Failed to delete file with key: " + key + " - " + e.awsErrorDetails().errorMessage()
            );
        }
    }

    private boolean fileExists(String bucketName, String key) {
        try {
            s3Client.headObject(builder -> builder.bucket(bucketName).key(key));
            return true;
        }catch (S3Exception e) {
            return false;
        }
    }

    public boolean isValidFile(MultipartFile file) throws S3FunctionalException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new S3FunctionalException(S3FunctionalExceptionType.INVALID_FILE_EXTENSION);
        }
        long fileSize = file.getSize();

        boolean isValidExtension = validateExtension(file);
        boolean isValidSize = isSizeValid(fileSize);

        if (!isValidExtension && !isValidSize) {
            throw new S3FunctionalException(S3FunctionalExceptionType.MULTIPLE_EXCEPTIONS);
        } else if (!isValidExtension) {
            throw new S3FunctionalException(S3FunctionalExceptionType.INVALID_FILE_EXTENSION);
        } else if (!isValidSize) {
            throw new S3FunctionalException(S3FunctionalExceptionType.FILE_TOO_LARGE);
        }
        return true;
    }

    private boolean bucketExists() {
        try{
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(awsConfigProperties.getBucketName())
                    .build();
            s3Client.headBucket(headBucketRequest);
            return true;
        }catch (NoSuchBucketException e) {
            return false;
        } catch (S3Exception e) {
            System.err.println("Error checking bucket: " + e.awsErrorDetails().errorMessage());
            return false;
        }
    }

    private boolean validateExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null && isAllowedExtension(fileName);
    }

    private boolean isAllowedExtension(String fileName) {
        String fileExtension = getFileExtension(fileName);
        List<String> allowedExtensions = awsConfigProperties.getAllowedExtensions();
        return allowedExtensions.contains(fileExtension.toLowerCase());
    }
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private boolean isSizeValid(long fileSize) {
        double maxSize = parseSize(awsConfigProperties.getMaxFileSize());
        return fileSize <= maxSize;
    }

    private double parseSize(String size) {
        size = size.trim().toUpperCase();
        double value = Double.parseDouble(size.substring(0, size.length() - 2));
        String unit = size.substring(size.length() - 2);
        switch (unit) {
            case "KB":
                value *= 1024;
                break;
            case "MB":
                value *= 1024 * 1024;
                break;
            case "GB":
                value *= 1024 * 1024 * 1024;
                break;
            default:
                break;
        }
        return value;
    }

    private String generatePresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(awsConfigProperties.getBucketName())
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

}
