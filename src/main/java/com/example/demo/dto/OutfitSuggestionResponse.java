package com.example.demo.dto;

import java.util.List;

public record OutfitSuggestionResponse(
    String occasion,
    String suggestion,
    List<WardrobeItemDto> suggestedItems
) {
    public static OutfitSuggestionResponseBuilder builder() {
        return new OutfitSuggestionResponseBuilder();
    }

    public static class OutfitSuggestionResponseBuilder {
        private String occasion;
        private String suggestion;
        private List<WardrobeItemDto> suggestedItems;

        public OutfitSuggestionResponseBuilder occasion(String occasion) { this.occasion = occasion; return this; }
        public OutfitSuggestionResponseBuilder suggestion(String suggestion) { this.suggestion = suggestion; return this; }
        public OutfitSuggestionResponseBuilder suggestedItems(List<WardrobeItemDto> items) { this.suggestedItems = items; return this; }
        public OutfitSuggestionResponse build() {
            return new OutfitSuggestionResponse(occasion, suggestion, suggestedItems);
        }
    }
}
