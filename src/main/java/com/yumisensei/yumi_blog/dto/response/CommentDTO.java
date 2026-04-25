package com.yumisensei.yumi_blog.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentDTO {
    private UUID id;
    private String name;
    private String content;
    private LocalDateTime createdAt;

    public CommentDTO(UUID id, String name, String content, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

