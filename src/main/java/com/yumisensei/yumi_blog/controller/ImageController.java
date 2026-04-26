package com.yumisensei.yumi_blog.controller;

import com.yumisensei.yumi_blog.entity.Image;
import com.yumisensei.yumi_blog.exception.ResourceNotFoundException;
import com.yumisensei.yumi_blog.repository.ImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageRepository imageRepository;

    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable UUID id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(image.getData());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file)
            throws IOException {
        Image image = new Image();
        image.setContentType(file.getContentType());
        image.setData(file.getBytes());

        Image saved = imageRepository.save(image);

        String url = "/api/images/" + saved.getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("url", url));
    }
}

