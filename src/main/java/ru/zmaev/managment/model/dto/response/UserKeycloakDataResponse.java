package ru.zmaev.managment.model.dto.response;

import lombok.Data;

@Data
public class UserKeycloakDataResponse {
    private String keycloakId;
    private String username;
    private String email;
    private String roleName;
}
