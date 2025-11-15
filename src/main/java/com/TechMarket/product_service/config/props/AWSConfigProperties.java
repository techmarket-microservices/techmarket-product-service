package com.TechMarket.product_service.config.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "aws")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AWSConfigProperties {
    private Credentials credentials;
    private String bucketName;
    private List<String> allowedExtensions = new ArrayList<>();
    private String maxFileSize;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }
}
