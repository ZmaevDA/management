package ru.zmaev.managment.auth;

import ru.zmaev.managment.model.dto.response.UserKeycloakDataResponse;
import ru.zmaev.managment.model.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(UserKeycloakDataResponse dto);
}
