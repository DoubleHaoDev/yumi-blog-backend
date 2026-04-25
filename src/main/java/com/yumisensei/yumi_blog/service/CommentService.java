package com.yumisensei.yumi_blog.service;

import com.yumisensei.yumi_blog.dto.request.CreateCommentRequest;
import com.yumisensei.yumi_blog.dto.response.CommentDTO;
import com.yumisensei.yumi_blog.entity.Comment;
import com.yumisensei.yumi_blog.entity.Post;
import com.yumisensei.yumi_blog.exception.ResourceNotFoundException;
import com.yumisensei.yumi_blog.mapper.DtoMapper;
import com.yumisensei.yumi_blog.repository.CommentRepository;
import com.yumisensei.yumi_blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public List<CommentDTO> getCommentsByPostSlug(String slug) {
        return commentRepository.findByPostSlugOrderByCreatedAtDesc(slug).stream()
                .map(DtoMapper::toCommentDTO)
                .toList();
    }

    public CommentDTO createComment(CreateCommentRequest request) {
        Post post = postRepository.findBySlug(request.getSlug())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with slug: " + request.getSlug()));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setName(request.getName());
        comment.setEmail(request.getEmail());
        comment.setContent(request.getContent());

        return DtoMapper.toCommentDTO(commentRepository.save(comment));
    }
}

