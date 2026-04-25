package com.yumisensei.yumi_blog.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PostListDTO {
    private UUID id;
    private String title;
    private String slug;
    private String excerpt;
    private String featuredImage;
    private Boolean featuredPost;
    private LocalDateTime createdAt;
    private AuthorDTO author;
    private List<CategoryDTO> categories;

    public PostListDTO(UUID id, String title, String slug, String excerpt, String featuredImage,
                       Boolean featuredPost, LocalDateTime createdAt, AuthorDTO author,
                       List<CategoryDTO> categories) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.excerpt = excerpt;
        this.featuredImage = featuredImage;
        this.featuredPost = featuredPost;
        this.createdAt = createdAt;
        this.author = author;
        this.categories = categories;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getSlug() { return slug; }
    public String getExcerpt() { return excerpt; }
    public String getFeaturedImage() { return featuredImage; }
    public Boolean getFeaturedPost() { return featuredPost; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public AuthorDTO getAuthor() { return author; }
    public List<CategoryDTO> getCategories() { return categories; }
}

