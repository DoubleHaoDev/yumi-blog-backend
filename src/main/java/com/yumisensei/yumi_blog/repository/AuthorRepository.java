package com.yumisensei.yumi_blog.repository;

import com.yumisensei.yumi_blog.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
}

