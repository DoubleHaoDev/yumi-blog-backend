package com.yumisensei.yumi_blog.repository;

import com.yumisensei.yumi_blog.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}

