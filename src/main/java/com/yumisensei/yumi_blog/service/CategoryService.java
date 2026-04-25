package com.yumisensei.yumi_blog.service;

import com.yumisensei.yumi_blog.dto.response.CategoryDTO;
import com.yumisensei.yumi_blog.dto.response.PostListDTO;
import com.yumisensei.yumi_blog.exception.ResourceNotFoundException;
import com.yumisensei.yumi_blog.mapper.DtoMapper;
import com.yumisensei.yumi_blog.repository.CategoryRepository;
import com.yumisensei.yumi_blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    public CategoryService(CategoryRepository categoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(DtoMapper::toCategoryDTO)
                .toList();
    }

    public List<PostListDTO> getPostsByCategory(String slug) {
        categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + slug));

        return postRepository.findByCategoriesSlug(slug).stream()
                .map(DtoMapper::toPostListDTO)
                .toList();
    }
}

