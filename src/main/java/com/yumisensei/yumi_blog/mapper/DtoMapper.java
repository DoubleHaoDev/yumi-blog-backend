package com.yumisensei.yumi_blog.mapper;

import com.yumisensei.yumi_blog.dto.response.AuthorDTO;
import com.yumisensei.yumi_blog.dto.response.CategoryDTO;
import com.yumisensei.yumi_blog.dto.response.CommentDTO;
import com.yumisensei.yumi_blog.dto.response.PostDetailDTO;
import com.yumisensei.yumi_blog.dto.response.PostListDTO;
import com.yumisensei.yumi_blog.entity.Author;
import com.yumisensei.yumi_blog.entity.Category;
import com.yumisensei.yumi_blog.entity.Comment;
import com.yumisensei.yumi_blog.entity.Post;

import java.util.List;

public class DtoMapper {

    private DtoMapper() {}

    public static AuthorDTO toAuthorDTO(Author author) {
        return new AuthorDTO(
                author.getId(),
                author.getName(),
                author.getBio(),
                author.getPhotoUrl()
        );
    }

    public static CategoryDTO toCategoryDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getSlug()
        );
    }

    public static PostListDTO toPostListDTO(Post post) {
        List<CategoryDTO> categories = post.getCategories().stream()
                .map(DtoMapper::toCategoryDTO)
                .toList();

        return new PostListDTO(
                post.getId(),
                post.getTitle(),
                post.getSlug(),
                post.getExcerpt(),
                post.getFeaturedImageUrl(),
                post.getFeaturedPost(),
                post.getCreatedAt(),
                toAuthorDTO(post.getAuthor()),
                categories
        );
    }

    public static PostDetailDTO toPostDetailDTO(Post post) {
        List<CategoryDTO> categories = post.getCategories().stream()
                .map(DtoMapper::toCategoryDTO)
                .toList();

        return new PostDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getSlug(),
                post.getExcerpt(),
                post.getContent(),
                post.getFeaturedImageUrl(),
                post.getFeaturedPost(),
                post.getCreatedAt(),
                toAuthorDTO(post.getAuthor()),
                categories
        );
    }

    public static CommentDTO toCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getName(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}


