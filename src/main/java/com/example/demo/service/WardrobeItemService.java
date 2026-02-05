package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.WardrobeItemDto;
import com.example.demo.model.WardrobeItem;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class WardrobeItemService {

    private static final String COLLECTION_NAME = "wardrobeItems";

    private final S3ImageUploadService s3ImageUploadService;

    public WardrobeItemService(S3ImageUploadService s3ImageUploadService) {
        this.s3ImageUploadService = s3ImageUploadService;
    }

    public String createWardrobeItem(WardrobeItemDto wardrobeItemDto) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        WardrobeItem wardrobeItem = new WardrobeItem();
        wardrobeItem.setName(wardrobeItemDto.getName());
        wardrobeItem.setCategory(wardrobeItemDto.getCategory());
        wardrobeItem.setColor(wardrobeItemDto.getColor());
        wardrobeItem.setImageUrl(wardrobeItemDto.getImageUrl());
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(COLLECTION_NAME).document().set(wardrobeItem);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    /**
     * Upload image to S3 and create wardrobe item in Firebase with the image URL.
     */
    public WardrobeItemDto createWardrobeItemWithImage(String userId, MultipartFile file,
                                                        String name, String category, String color) throws Exception {
        String imageUrl = s3ImageUploadService.uploadImage(file, userId);

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document();

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("name", name);
        data.put("category", category);
        data.put("color", color != null ? color : "");
        data.put("imageUrl", imageUrl);

        docRef.set(data).get();

        WardrobeItemDto dto = new WardrobeItemDto();
        dto.setId(docRef.getId());
        dto.setName(name);
        dto.setCategory(category);
        dto.setColor(color);
        dto.setImageUrl(imageUrl);
        return dto;
    }

    /** Fetch all wardrobe items for a user (for AI suggestions). */
    public List<WardrobeItemDto> getWardrobeByUserId(String userId) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get();
        List<WardrobeItemDto> list = new ArrayList<>();
        for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
            WardrobeItemDto dto = new WardrobeItemDto();
            dto.setId(doc.getId());
            dto.setName((String) doc.get("name"));
            dto.setCategory((String) doc.get("category"));
            dto.setColor((String) doc.get("color"));
            dto.setImageUrl((String) doc.get("imageUrl"));
            list.add(dto);
        }
        return list;
    }

    // Other CRUD operations...
}
