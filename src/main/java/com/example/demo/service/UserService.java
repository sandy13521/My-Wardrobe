package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String COLLECTION_NAME = "users";

    public String createUser(UserDto userDto) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setGender(userDto.getGender());
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(COLLECTION_NAME).document(userDto.getUsername()).set(user);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    // Other CRUD operations... (get, update, delete)
}
