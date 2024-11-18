package ru.zmaev.managment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zmaev.managment.controller.openApi.UserOpenApi;
import ru.zmaev.managment.model.dto.response.UserResponse;
import ru.zmaev.managment.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController implements UserOpenApi {
    private final UserServiceImpl userService;

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserResponse>> loadAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(userService.loadAll(pageNumber, pageSize));
    }

    @Override
    @GetMapping("/me")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserResponse> loadCurrentUserData() {
        return ResponseEntity.ok(userService.loadCurrentUserData());
    }
}
