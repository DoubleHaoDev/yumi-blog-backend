package com.yumisensei.yumi_blog.dto.response;

import java.util.UUID;

public class CategoryDTO {
    private UUID id;
    private String name;
    private String slug;

    public CategoryDTO(UUID id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
}

