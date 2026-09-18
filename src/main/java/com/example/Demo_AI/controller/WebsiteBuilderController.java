package com.example.Demo_AI.controller;

import com.example.Demo_AI.service.WebsiteBuilderService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/website")
@CrossOrigin(origins = "*")
public class WebsiteBuilderController {
    private final WebsiteBuilderService websiteService;

    public WebsiteBuilderController(WebsiteBuilderService websiteService) {
        this.websiteService = websiteService;
    }

    @PostMapping
    public ResponseEntity<ByteArrayResource> generateWebsite(@RequestBody String message) throws IOException {
        byte[] zipBytes = websiteService.generateZip(message);
        ByteArrayResource resource = new ByteArrayResource(zipBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"website.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(zipBytes.length)
                .body(resource);
    }
}