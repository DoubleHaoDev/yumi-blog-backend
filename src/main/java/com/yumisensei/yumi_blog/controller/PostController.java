package com.yumisensei.yumi_blog.controller;

import com.yumisensei.yumi_blog.dto.response.AdjacentPostDTO;
import com.yumisensei.yumi_blog.dto.response.PostDetailDTO;
import com.yumisensei.yumi_blog.dto.response.PostListDTO;
import com.yumisensei.yumi_blog.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostListDTO>> getPosts(
            @RequestParam(required = false) Boolean featured) {
        if (Boolean.TRUE.equals(featured)) {
            return ResponseEntity.ok(postService.getFeaturedPosts());
        }
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<PostListDTO>> getRecentPosts() {
        return ResponseEntity.ok(postService.getRecentPosts());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PostDetailDTO> getPostBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(postService.getPostBySlug(slug));
    }

    @GetMapping("/{slug}/similar")
    public ResponseEntity<List<PostListDTO>> getSimilarPosts(@PathVariable String slug) {
        return ResponseEntity.ok(postService.getSimilarPosts(slug));
    }

    @GetMapping("/{slug}/adjacent")
    public ResponseEntity<AdjacentPostDTO> getAdjacentPosts(@PathVariable String slug) {
        return ResponseEntity.ok(postService.getAdjacentPosts(slug));
    }
}

