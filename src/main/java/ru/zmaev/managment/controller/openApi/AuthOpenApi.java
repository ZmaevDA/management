package ru.zmaev.managment.controller.openApi;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import ru.zmaev.managment.controller.handler.ExceptionResponse;
import ru.zmaev.managment.model.dto.response.UserKeycloakDataResponse;
import ru.zmaev.managment.model.dto.response.UserResponse;

@Tag(name = "Auth API")
public interface AuthOpenApi {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success registration",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already registered",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Register in system",
            description = "This endpoint is used by keycloak only"
    )
    ResponseEntity<UserResponse> register(UserKeycloakDataResponse dto);
}
