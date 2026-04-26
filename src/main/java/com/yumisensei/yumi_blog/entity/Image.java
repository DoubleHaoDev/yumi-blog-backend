package com.yumisensei.yumi_blog.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "data", nullable = false, columnDefinition = "BYTEA")
    private byte[] data;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
}

