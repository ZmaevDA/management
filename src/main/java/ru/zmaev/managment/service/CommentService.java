package ru.zmaev.managment.service;

import ru.zmaev.managment.model.dto.request.CommentRequest;
import ru.zmaev.managment.model.dto.response.CommentResponse;
import ru.zmaev.managment.model.dto.response.TaskWithCommentsResponse;

import java.util.UUID;

public interface CommentService {
    TaskWithCommentsResponse loadAllCommentsByTaskIdAndAuthorId(UUID id, UUID authorId, int pageNumber, int pageSize);

    CommentResponse create(UUID taskId, CommentRequest request);

    CommentResponse update(UUID commentId, CommentRequest request);

    void deleteById(UUID commentId);
}
