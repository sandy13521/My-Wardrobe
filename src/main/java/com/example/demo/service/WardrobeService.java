package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.WardrobeDto;
import com.example.demo.dto.WardrobeItemDto;
import com.example.demo.model.Wardrobe;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class WardrobeService {

    private static final String COLLECTION_NAME = "wardrobes";

    private final WardrobeItemService wardrobeItemService;

    public WardrobeService(WardrobeItemService wardrobeItemService) {
        this.wardrobeItemService = wardrobeItemService;
    }

    public String createWardrobe(WardrobeDto wardrobeDto) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        Wardrobe wardrobe = new Wardrobe();
        wardrobe.setName(wardrobeDto.getName());
        wardrobe.setUserId(wardrobeDto.getUserId());
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(COLLECTION_NAME).document().set(wardrobe);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    /** Returns wardrobe items for the user (for AI outfit suggestions). */
    public List<WardrobeItemDto> getWardrobe(Long userId) throws Exception {
        return wardrobeItemService.getWardrobeByUserId(String.valueOf(userId));
    }

    // Other CRUD operations...
}
