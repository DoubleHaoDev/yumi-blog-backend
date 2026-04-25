package com.yumisensei.yumi_blog.controller;

import com.yumisensei.yumi_blog.dto.request.CreateCommentRequest;
import com.yumisensei.yumi_blog.dto.response.CommentDTO;
import com.yumisensei.yumi_blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/posts/{slug}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable String slug) {
        return ResponseEntity.ok(commentService.getCommentsByPostSlug(slug));
    }

    @PostMapping("/comments")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(request));
    }
}

