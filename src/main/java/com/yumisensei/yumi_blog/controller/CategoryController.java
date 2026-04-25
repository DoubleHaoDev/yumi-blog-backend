package com.yumisensei.yumi_blog.controller;

import com.yumisensei.yumi_blog.dto.response.CategoryDTO;
import com.yumisensei.yumi_blog.dto.response.PostListDTO;
import com.yumisensei.yumi_blog.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{slug}/posts")
    public ResponseEntity<List<PostListDTO>> getPostsByCategory(@PathVariable String slug) {
        return ResponseEntity.ok(categoryService.getPostsByCategory(slug));
    }
}

