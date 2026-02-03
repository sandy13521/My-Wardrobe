package com.example.demo.repository;

import com.example.demo.entity.WardrobeItem;
import com.example.demo.entity.WardrobeItem.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WardrobeItemRepository extends JpaRepository<WardrobeItem, Long> {
    List<WardrobeItem> findByUserId(Long userId);
    List<WardrobeItem> findByUserIdAndCategory(Long userId, ItemCategory category);
}
