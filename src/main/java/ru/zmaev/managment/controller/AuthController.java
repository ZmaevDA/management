package ru.zmaev.managment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zmaev.managment.model.dto.response.UserKeycloakDataResponse;
import ru.zmaev.managment.model.dto.response.UserResponse;
import ru.zmaev.managment.auth.AuthService;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserKeycloakDataResponse dto) {
        return ResponseEntity.ok(authService.register(dto));
    }
}
