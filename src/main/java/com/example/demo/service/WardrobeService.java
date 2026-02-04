package com.example.demo.service;

import com.example.demo.dto.WardrobeDto;
import com.example.demo.model.Wardrobe;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

@Service
public class WardrobeService {

    private static final String COLLECTION_NAME = "wardrobes";

    public String createWardrobe(WardrobeDto wardrobeDto) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        Wardrobe wardrobe = new Wardrobe();
        wardrobe.setName(wardrobeDto.getName());
        wardrobe.setUserId(wardrobeDto.getUserId());
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(COLLECTION_NAME).document().set(wardrobe);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    // Other CRUD operations...
}
