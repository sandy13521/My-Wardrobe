package com.example.demo.dto;

import com.example.demo.entity.WardrobeItem.ItemCategory;

public record WardrobeItemDto(
    Long id,
    String name,
    ItemCategory category,
    String description,
    String color,
    String imageUrl
) {
    public static WardrobeItemDtoBuilder builder() {
        return new WardrobeItemDtoBuilder();
    }

    public static class WardrobeItemDtoBuilder {
        private Long id;
        private String name;
        private ItemCategory category;
        private String description;
        private String color;
        private String imageUrl;

        public WardrobeItemDtoBuilder id(Long id) { this.id = id; return this; }
        public WardrobeItemDtoBuilder name(String name) { this.name = name; return this; }
        public WardrobeItemDtoBuilder category(ItemCategory category) { this.category = category; return this; }
        public WardrobeItemDtoBuilder description(String description) { this.description = description; return this; }
        public WardrobeItemDtoBuilder color(String color) { this.color = color; return this; }
        public WardrobeItemDtoBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public WardrobeItemDto build() {
            return new WardrobeItemDto(id, name, category, description, color, imageUrl);
        }
    }
}
