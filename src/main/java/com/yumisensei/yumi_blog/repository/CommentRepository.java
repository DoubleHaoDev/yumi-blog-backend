package com.yumisensei.yumi_blog.repository;

import com.yumisensei.yumi_blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByPostSlugOrderByCreatedAtDesc(String slug);
}

