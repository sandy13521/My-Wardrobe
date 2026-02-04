package com.example.demo.service;

import com.example.demo.dto.WardrobeItemDto;
import com.example.demo.model.WardrobeItem;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

@Service
public class WardrobeItemService {

    private static final String COLLECTION_NAME = "wardrobeItems";

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

    // Other CRUD operations...
}
