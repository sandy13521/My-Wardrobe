package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.region:eu-north-1}")
    private String region;

    @Value("${aws.s3.access-key-id:}")
    private String accessKeyId;

    @Value("${aws.s3.secret-access-key:}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client() {
        AwsCredentialsProvider credentials = (accessKeyId != null && !accessKeyId.isBlank())
                ? () -> AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                : DefaultCredentialsProvider.create();

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentials)
                .build();
    }
}
