package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wardrobe_items")
public class WardrobeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemCategory category;

    @Column(length = 500)
    private String description;

    private String color;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum ItemCategory {
        SHIRT, PANTS, DRESS, JACKET, SHOES, ACCESSORY, HAT, BAG, OTHER
    }

    public WardrobeItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ItemCategory getCategory() { return category; }
    public void setCategory(ItemCategory category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public static WardrobeItemBuilder builder() {
        return new WardrobeItemBuilder();
    }

    public static class WardrobeItemBuilder {
        private final WardrobeItem item = new WardrobeItem();
        public WardrobeItemBuilder name(String name) { item.setName(name); return this; }
        public WardrobeItemBuilder category(ItemCategory category) { item.setCategory(category); return this; }
        public WardrobeItemBuilder description(String description) { item.setDescription(description); return this; }
        public WardrobeItemBuilder color(String color) { item.setColor(color); return this; }
        public WardrobeItemBuilder imageUrl(String imageUrl) { item.setImageUrl(imageUrl); return this; }
        public WardrobeItemBuilder user(User user) { item.setUser(user); return this; }
        public WardrobeItem build() { return item; }
    }
}
