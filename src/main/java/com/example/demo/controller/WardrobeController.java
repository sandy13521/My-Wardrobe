package com.example.demo.controller;

import com.example.demo.dto.WardrobeDto;
import com.example.demo.service.WardrobeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wardrobes")
public class WardrobeController {

    @Autowired
    private WardrobeService wardrobeService;

    @PostMapping
    public String createWardrobe(@RequestBody WardrobeDto wardrobeDto) throws Exception {
        return wardrobeService.createWardrobe(wardrobeDto);
    }

    // Other endpoints...
}
