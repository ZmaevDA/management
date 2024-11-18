package ru.zmaev.managment.controller.openApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ru.zmaev.managment.controller.handler.ExceptionResponse;
import ru.zmaev.managment.model.dto.request.TaskCreateRequest;
import ru.zmaev.managment.model.dto.request.TaskUpdateRequest;
import ru.zmaev.managment.model.dto.response.TaskFilterRequest;
import ru.zmaev.managment.model.dto.response.TaskResponse;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;

import java.util.UUID;

@Tag(name = "Task API")
public interface TaskOpenApi {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response for Page of TaskResponse",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Load all task with filters",
            description = "All access"
    )
    ResponseEntity<Page<TaskResponse>> loadAll(TaskFilterRequest filter, int pageNumber, int pageSize);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response for TaskResponse",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Load task by id",
            description = "All access"
    )
    ResponseEntity<TaskResponse> loadById(UUID id);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response for TaskResponse",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Create new task",
            description = "All access"
    )
    ResponseEntity<TaskResponse> create(@Valid TaskCreateRequest request);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success assign for task",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task or user not found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No access",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Task is already assigned",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Assign task",
            description = "All access"
    )
    ResponseEntity<TaskResponse> assign(UUID taskId, UUID assignerId);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success unassign from task",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No access",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Task is not assigned",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Unassign task",
            description = "All access"
    )
    ResponseEntity<TaskResponse> unassign(UUID taskId);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success change task status",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No access",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Change task status",
            description = "All access"
    )
    ResponseEntity<TaskResponse> changeStatus(UUID id, StatusType statusType);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success change task priority",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No access",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Change task priority",
            description = "All access"
    )
    ResponseEntity<TaskResponse> changePriority(UUID id, PriorityType priorityType);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success task update",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No access",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Update task",
            description = "All access"
    )
    ResponseEntity<TaskResponse> update(UUID id, @Valid TaskUpdateRequest request);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success task deletion",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No access",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Delete task by id",
            description = "All access"
    )
    ResponseEntity<Void> deleteById(UUID id);
}
