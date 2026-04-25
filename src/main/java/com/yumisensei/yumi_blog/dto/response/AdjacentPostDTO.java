package com.yumisensei.yumi_blog.dto.response;

public class AdjacentPostDTO {
    private PostListDTO next;
    private PostListDTO previous;

    public AdjacentPostDTO(PostListDTO next, PostListDTO previous) {
        this.next = next;
        this.previous = previous;
    }

    public PostListDTO getNext() { return next; }
    public PostListDTO getPrevious() { return previous; }
}

