package com.yumisensei.yumi_blog.service;

import com.yumisensei.yumi_blog.dto.response.AdjacentPostDTO;
import com.yumisensei.yumi_blog.dto.response.PostDetailDTO;
import com.yumisensei.yumi_blog.dto.response.PostListDTO;
import com.yumisensei.yumi_blog.entity.Post;
import com.yumisensei.yumi_blog.entity.Category;
import com.yumisensei.yumi_blog.exception.ResourceNotFoundException;
import com.yumisensei.yumi_blog.mapper.DtoMapper;
import com.yumisensei.yumi_blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostListDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(DtoMapper::toPostListDTO)
                .toList();
    }

    public List<PostListDTO> getFeaturedPosts() {
        return postRepository.findByFeaturedPostTrue().stream()
                .map(DtoMapper::toPostListDTO)
                .toList();
    }

    public List<PostListDTO> getRecentPosts() {
        return postRepository.findTop3ByOrderByCreatedAtDesc().stream()
                .map(DtoMapper::toPostListDTO)
                .toList();
    }

    public PostDetailDTO getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with slug: " + slug));
        return DtoMapper.toPostDetailDTO(post);
    }

    public List<PostListDTO> getSimilarPosts(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with slug: " + slug));

        List<String> categorySlugs = post.getCategories().stream()
                .map(Category::getSlug)
                .toList();

        return postRepository.findSimilarPosts(categorySlugs, slug).stream()
                .map(DtoMapper::toPostListDTO)
                .toList();
    }

    public AdjacentPostDTO getAdjacentPosts(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with slug: " + slug));

        Optional<Post> next = postRepository
                .findFirstByCreatedAtGreaterThanAndSlugNotOrderByCreatedAtAsc(post.getCreatedAt(), slug);

        Optional<Post> previous = postRepository
                .findFirstByCreatedAtLessThanAndSlugNotOrderByCreatedAtDesc(post.getCreatedAt(), slug);

        return new AdjacentPostDTO(
                next.map(DtoMapper::toPostListDTO).orElse(null),
                previous.map(DtoMapper::toPostListDTO).orElse(null)
        );
    }
}



