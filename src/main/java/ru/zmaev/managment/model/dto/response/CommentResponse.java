package ru.zmaev.managment.model.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentResponse {
    private String id;
    private String content;
    private UserResponse author;
    private Instant createdAt;
    private Instant updatedAt;
}
