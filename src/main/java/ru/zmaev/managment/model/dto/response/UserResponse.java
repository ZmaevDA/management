package ru.zmaev.managment.model.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private UUID keycloakId;
    private String username;
    private String email;
}
