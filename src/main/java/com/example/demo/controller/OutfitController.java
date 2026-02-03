package com.example.demo.controller;

import com.example.demo.dto.OutfitSuggestionRequest;
import com.example.demo.dto.OutfitSuggestionResponse;
import com.example.demo.service.AiOutfitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/outfit")
public class OutfitController {

    private final AiOutfitService aiOutfitService;

    public OutfitController(AiOutfitService aiOutfitService) {
        this.aiOutfitService = aiOutfitService;
    }

    @PostMapping("/suggest")
    public ResponseEntity<OutfitSuggestionResponse> suggestOutfit(
            @RequestBody OutfitSuggestionRequest request) {
        return ResponseEntity.ok(aiOutfitService.suggestOutfit(request));
    }

    @GetMapping("/suggest/{userId}")
    public ResponseEntity<OutfitSuggestionResponse> suggestOutfitGet(
            @PathVariable Long userId,
            @RequestParam String occasion) {
        OutfitSuggestionRequest request = new OutfitSuggestionRequest(userId, occasion);
        return ResponseEntity.ok(aiOutfitService.suggestOutfit(request));
    }
}
