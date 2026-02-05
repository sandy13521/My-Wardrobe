package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.WardrobeDto;
import com.example.demo.dto.WardrobeItemDto;
import com.example.demo.service.WardrobeItemService;
import com.example.demo.service.WardrobeService;

@RestController
@RequestMapping("/api/wardrobes")
public class WardrobeController {

    @Autowired
    private WardrobeService wardrobeService;

    @Autowired
    private WardrobeItemService wardrobeItemService;

    @PostMapping
    public String createWardrobe(@RequestBody WardrobeDto wardrobeDto) throws Exception {
        return wardrobeService.createWardrobe(wardrobeDto);
    }

    /**
     * Upload a wardrobe item with image. Image is stored in S3; metadata + image URL in Firebase.
     * Form params: file (required), name (required), category (required), color (optional)
     */
    @PostMapping("/users/{userId}/items/upload")
    public ResponseEntity<WardrobeItemDto> uploadItemWithImage(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam(value = "color", required = false) String color) throws Exception {
        WardrobeItemDto created = wardrobeItemService.createWardrobeItemWithImage(userId, file, name, category, color);
        return ResponseEntity.ok(created);
    }

    // Other endpoints...
}
