package com.example.demo.service;

import com.example.demo.dto.OutfitSuggestionRequest;
import com.example.demo.dto.OutfitSuggestionResponse;
import com.example.demo.dto.WardrobeItemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiOutfitService {

    private static final Logger log = LoggerFactory.getLogger(AiOutfitService.class);

    private final WardrobeService wardrobeService;
    private final RestTemplate restTemplate;

    @Value("${openai.api.key:}")
    private String openAiApiKey;

    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String openAiApiUrl;

    public AiOutfitService(WardrobeService wardrobeService, RestTemplate restTemplate) {
        this.wardrobeService = wardrobeService;
        this.restTemplate = restTemplate;
    }

    public OutfitSuggestionResponse suggestOutfit(OutfitSuggestionRequest request) {
        List<WardrobeItemDto> wardrobe = wardrobeService.getWardrobe(request.userId());

        if (wardrobe.isEmpty()) {
            return OutfitSuggestionResponse.builder()
                    .occasion(request.occasion())
                    .suggestion("Your wardrobe is empty. Add some clothes first!")
                    .suggestedItems(Collections.emptyList())
                    .build();
        }

        String suggestion = getAiSuggestion(wardrobe, request.occasion());
        List<WardrobeItemDto> suggestedItems = selectItemsForOccasion(wardrobe, request.occasion());

        return OutfitSuggestionResponse.builder()
                .occasion(request.occasion())
                .suggestion(suggestion)
                .suggestedItems(suggestedItems)
                .build();
    }

    private String getAiSuggestion(List<WardrobeItemDto> wardrobe, String occasion) {
        if (openAiApiKey == null || openAiApiKey.isBlank()) {
            return getFallbackSuggestion(wardrobe, occasion);
        }

        try {
            String wardrobeDescription = buildWardrobeDescription(wardrobe);
            String prompt = String.format(
                "You are a fashion stylist. Based on this wardrobe: %s\n" +
                "Suggest an outfit for: %s. Keep response concise (2-3 sentences).",
                wardrobeDescription, occasion
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "max_tokens", 150
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(openAiApiUrl, entity, Map.class);

            if (response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            log.error("Error calling OpenAI API: {}", e.getMessage());
        }

        return getFallbackSuggestion(wardrobe, occasion);
    }

    private String getFallbackSuggestion(List<WardrobeItemDto> wardrobe, String occasion) {
        return switch (occasion.toLowerCase()) {
            case "wedding" -> "For a wedding, choose formal attire. Consider a dress or suit with elegant accessories.";
            case "bike ride" -> "For biking, go with comfortable sportswear. Athletic pants and a breathable shirt work great.";
            case "beach day" -> "For the beach, opt for light, breezy clothing. Shorts, a tank top, and sandals are perfect.";
            case "formal" -> "For formal events, a suit or elegant dress with polished shoes creates the right impression.";
            case "casual" -> "For casual outings, jeans with a nice shirt or blouse is always a safe choice.";
            default -> "Choose comfortable clothes appropriate for the weather and activity level.";
        };
    }

    private List<WardrobeItemDto> selectItemsForOccasion(List<WardrobeItemDto> wardrobe, String occasion) {
        List<WardrobeItemDto> selected = new ArrayList<>();

        Map<String, List<String>> occasionCategories = Map.of(
            "wedding", List.of("DRESS", "SHIRT", "PANTS", "SHOES", "ACCESSORY"),
            "bike ride", List.of("SHIRT", "PANTS", "SHOES"),
            "beach day", List.of("SHIRT", "PANTS", "HAT", "ACCESSORY"),
            "formal", List.of("SHIRT", "PANTS", "JACKET", "SHOES"),
            "casual", List.of("SHIRT", "PANTS", "SHOES")
        );

        List<String> targetCategories = occasionCategories.getOrDefault(
            occasion.toLowerCase(),
            List.of("SHIRT", "PANTS", "SHOES")
        );

        for (String cat : targetCategories) {
            wardrobe.stream()
                .filter(item -> item.category().name().equals(cat))
                .findFirst()
                .ifPresent(selected::add);
        }

        return selected;
    }

    private String buildWardrobeDescription(List<WardrobeItemDto> wardrobe) {
        StringBuilder sb = new StringBuilder();
        for (WardrobeItemDto item : wardrobe) {
            sb.append(String.format("%s (%s, %s), ", item.name(), item.category(), item.color()));
        }
        return sb.toString();
    }
}
