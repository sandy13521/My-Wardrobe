package com.example.demo.controller;

import com.example.demo.dto.WardrobeItemDto;
import com.example.demo.entity.WardrobeItem.ItemCategory;
import com.example.demo.service.WardrobeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wardrobe")
public class WardrobeController {

    private final WardrobeService wardrobeService;
    private static final String UPLOAD_DIR = "uploads/";

    public WardrobeController(WardrobeService wardrobeService) {
        this.wardrobeService = wardrobeService;
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<WardrobeItemDto> addItem(
            @PathVariable Long userId,
            @RequestBody WardrobeItemDto itemDto) {
        return ResponseEntity.ok(wardrobeService.addItem(userId, itemDto));
    }

    @PostMapping("/{userId}/upload")
    public ResponseEntity<WardrobeItemDto> uploadItemWithImage(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("category") ItemCategory category,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "color", required = false) String color) throws IOException {

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        WardrobeItemDto itemDto = WardrobeItemDto.builder()
                .name(name)
                .category(category)
                .description(description)
                .color(color)
                .imageUrl("/uploads/" + filename)
                .build();

        return ResponseEntity.ok(wardrobeService.addItem(userId, itemDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<WardrobeItemDto>> getWardrobe(@PathVariable Long userId) {
        return ResponseEntity.ok(wardrobeService.getWardrobe(userId));
    }

    @GetMapping("/{userId}/category/{category}")
    public ResponseEntity<List<WardrobeItemDto>> getWardrobeByCategory(
            @PathVariable Long userId,
            @PathVariable ItemCategory category) {
        return ResponseEntity.ok(wardrobeService.getWardrobeByCategory(userId, category));
    }
}
