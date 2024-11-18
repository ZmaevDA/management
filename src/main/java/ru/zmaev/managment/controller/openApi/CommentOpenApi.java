package ru.zmaev.managment.controller.openApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import ru.zmaev.managment.controller.handler.ExceptionResponse;
import ru.zmaev.managment.model.dto.request.CommentRequest;
import ru.zmaev.managment.model.dto.response.CommentResponse;
import ru.zmaev.managment.model.dto.response.TaskWithCommentsResponse;

import java.util.UUID;

@Tag(name = "Comment API")
public interface CommentOpenApi {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response of task with comments",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskWithCommentsResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Load all task with comments",
            description = "All access"
    )
    ResponseEntity<TaskWithCommentsResponse> loadAllCommentsByTaskIdAndAuthorId(UUID id, UUID authorId, int pageNumber, int pageSize);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "New comment created successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Create new comment",
            description = "All access"
    )
    ResponseEntity<CommentResponse> create(UUID taskId, @Valid CommentRequest request);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment updated successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No access to update comment",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment not found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Update comment",
            description = "All access"
    )
    ResponseEntity<CommentResponse> update(UUID commentId, @Valid CommentRequest request);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment deleted successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No access to delete comment",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment not found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Delete comment",
            description = "All access"
    )
    ResponseEntity<Void> deleteById(UUID commentId);
}
