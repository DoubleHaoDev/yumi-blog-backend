package com.yumisensei.yumi_blog.repository;

import com.yumisensei.yumi_blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    Optional<Post> findBySlug(String slug);

    List<Post> findByFeaturedPostTrue();

    List<Post> findTop3ByOrderByCreatedAtDesc();

    List<Post> findByCategoriesSlug(String slug);

    // Similar posts: same categories, excluding current post, limit 3
    @Query("""
        SELECT DISTINCT p FROM Post p
        JOIN p.categories c
        WHERE c.slug IN :categorySlugs
        AND p.slug <> :slug
        ORDER BY p.createdAt DESC
        LIMIT 3
    """)
    List<Post> findSimilarPosts(@Param("categorySlugs") List<String> categorySlugs,
                                @Param("slug") String slug);

    // Next post (created after current)
    Optional<Post> findFirstByCreatedAtGreaterThanAndSlugNotOrderByCreatedAtAsc(
            LocalDateTime createdAt, String slug);

    // Previous post (created before current)
    Optional<Post> findFirstByCreatedAtLessThanAndSlugNotOrderByCreatedAtDesc(
            LocalDateTime createdAt, String slug);
}

