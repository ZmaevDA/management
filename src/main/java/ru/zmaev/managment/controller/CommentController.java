package ru.zmaev.managment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zmaev.managment.controller.openApi.CommentOpenApi;
import ru.zmaev.managment.model.dto.request.CommentRequest;
import ru.zmaev.managment.model.dto.response.CommentResponse;
import ru.zmaev.managment.model.dto.response.TaskWithCommentsResponse;
import ru.zmaev.managment.service.CommentService;

import java.util.UUID;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
public class CommentController implements CommentOpenApi {
    private final CommentService commentService;

    @Override
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskWithCommentsResponse> loadAllCommentsByTaskIdAndAuthorId(
            @PathVariable UUID taskId,
            @RequestParam(required = false) UUID authorId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(commentService.loadAllCommentsByTaskIdAndAuthorId(taskId, authorId, pageNumber, pageSize));
    }

    @PostMapping("/tasks/{taskId}")
    public ResponseEntity<CommentResponse> create(@PathVariable UUID taskId, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.create(taskId, request));
    }

    @Override
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> update(@PathVariable UUID commentId, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.update(commentId, request));
    }

    @Override
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID commentId) {
        commentService.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }
}
