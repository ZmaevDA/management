package ru.zmaev.managment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class CommentController implements CommentOpenApi {
    private final CommentService commentService;

    @Override
    @GetMapping("/tasks/{taskId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<TaskWithCommentsResponse> loadAllCommentsByTaskIdAndAuthorId(
            @PathVariable UUID taskId,
            @RequestParam(required = false) UUID authorId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(commentService.loadAllCommentsByTaskIdAndAuthorId(taskId, authorId, pageNumber, pageSize));
    }

    @PostMapping("/tasks/{taskId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CommentResponse> create(@PathVariable UUID taskId, @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.create(taskId, request));
    }

    @Override
    @PatchMapping("/{commentId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CommentResponse> update(@PathVariable UUID commentId, @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.update(commentId, request));
    }

    @Override
    @DeleteMapping("/{commentId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> deleteById(@PathVariable UUID commentId) {
        commentService.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }
}
