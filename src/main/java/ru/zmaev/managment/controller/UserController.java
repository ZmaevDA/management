package ru.zmaev.managment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zmaev.managment.model.dto.response.UserResponse;
import ru.zmaev.managment.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> loadCurrentUserData() {
        return ResponseEntity.ok(userService.loadCurrentUserData());
    }
}
