package ru.zmaev.managment.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Map;

public class IntegrationTestAuthUtils {
    public static JwtAuthenticationToken createBaseUserJwtAuthenticationToken(String userId) {
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("sub", "60670d49-a127-4a24-9a40-3aa2d7e19f83")
                .claim("email", "user2@example.com")
                .claim("name", "user2")
                .claim("realm_access", Map.of("roles", List.of("user")))
                .build();

        return new JwtAuthenticationToken(
                jwt,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
