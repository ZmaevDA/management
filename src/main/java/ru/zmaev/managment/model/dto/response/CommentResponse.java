package ru.zmaev.managment.model.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CommentResponse {
    private UUID id;
    private String content;
    private UserResponse author;
    private Instant createdAt;
    private Instant updatedAt;
}
