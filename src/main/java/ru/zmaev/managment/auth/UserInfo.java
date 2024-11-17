package ru.zmaev.managment.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserInfo {
    private UUID userId;
    private String jwt;
    private String username;
    private String email;
    private List<String> role;
}
