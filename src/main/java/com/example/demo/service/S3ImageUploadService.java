package com.example.demo.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3ImageUploadService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name:wardrobe-image-items}")
    private String bucketName;

    @Value("${aws.region:eu-north-1}")
    private String region;

    private static final long MAX_SIZE_BYTES = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_CONTENT_TYPES = {
            "image/jpeg", "image/png", "image/gif", "image/webp"
    };

    public S3ImageUploadService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Upload image to S3 and return the public URL.
     * Key format: users/{userId}/items/{uuid}_{originalFilename}
     */
    public String uploadImage(MultipartFile file, String userId) throws IOException {
        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".jpg";
        String key = "users/" + userId + "/items/" + UUID.randomUUID() + extension;

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }
        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new IllegalArgumentException("File size must not exceed 10MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !java.util.Arrays.asList(ALLOWED_CONTENT_TYPES).contains(contentType)) {
            throw new IllegalArgumentException("Allowed types: JPEG, PNG, GIF, WEBP");
        }
    }
}
