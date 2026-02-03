package com.example.demo.service;

import com.example.demo.dto.WardrobeItemDto;
import com.example.demo.entity.User;
import com.example.demo.entity.WardrobeItem;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WardrobeItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WardrobeService {

    private final WardrobeItemRepository wardrobeItemRepository;
    private final UserRepository userRepository;

    public WardrobeService(WardrobeItemRepository wardrobeItemRepository, UserRepository userRepository) {
        this.wardrobeItemRepository = wardrobeItemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WardrobeItemDto addItem(Long userId, WardrobeItemDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        WardrobeItem item = WardrobeItem.builder()
                .name(dto.name())
                .category(dto.category())
                .description(dto.description())
                .color(dto.color())
                .imageUrl(dto.imageUrl())
                .user(user)
                .build();

        WardrobeItem saved = wardrobeItemRepository.save(item);
        return toDto(saved);
    }

    public List<WardrobeItemDto> getWardrobe(Long userId) {
        return wardrobeItemRepository.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<WardrobeItemDto> getWardrobeByCategory(Long userId, WardrobeItem.ItemCategory category) {
        return wardrobeItemRepository.findByUserIdAndCategory(userId, category)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private WardrobeItemDto toDto(WardrobeItem item) {
        return WardrobeItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .category(item.getCategory())
                .description(item.getDescription())
                .color(item.getColor())
                .imageUrl(item.getImageUrl())
                .build();
    }
}
