package ru.zmaev.managment.controller.openApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ru.zmaev.managment.controller.handler.ExceptionResponse;
import ru.zmaev.managment.model.dto.response.UserResponse;

import java.util.UUID;

@Tag(name = "User API")
public interface UserOpenApi {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response for Page of UserResponse",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
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
            summary = "Load all users",
            description = "Access: ADMIN ONLY"
    )
    ResponseEntity<Page<UserResponse>> loadAll(int pageNumber, int pageSize);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success return UserResponse",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Load current user data",
            description = "All access"
    )
    ResponseEntity<UserResponse> loadCurrentUserData();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success return UserResponse",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
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
            summary = "Load user by id",
            description = "Access: ADMIN ONLY"
    )
    ResponseEntity<UserResponse> loadById(UUID id);
}
