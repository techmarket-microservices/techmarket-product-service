package com.TechMarket.product_service.config;

import com.TechMarket.product_service.config.props.AWSConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
    private final AWSConfigProperties awsConfigProperties;

    public S3Config(AWSConfigProperties awsConfigProperties) {
        this.awsConfigProperties = awsConfigProperties;
    }

    @Bean
    public S3Client s3Client(){
        return S3Client.builder()
                .region(Region.EU_WEST_3)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                awsConfigProperties.getCredentials().getAccessKey(),
                                awsConfigProperties.getCredentials().getSecretKey()
                        )
                ))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.EU_WEST_3)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                awsConfigProperties.getCredentials().getAccessKey(),
                                awsConfigProperties.getCredentials().getSecretKey()
                        )
                ))
                .build();
    }

}
