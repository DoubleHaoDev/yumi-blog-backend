package com.yumisensei.yumi_blog.dto.response;

import java.util.UUID;

public class AuthorDTO {
    private UUID id;
    private String name;
    private String bio;
    private String photoUrl;

    public AuthorDTO(UUID id, String name, String bio, String photoUrl) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.photoUrl = photoUrl;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getBio() { return bio; }
    public String getPhotoUrl() { return photoUrl; }
}

