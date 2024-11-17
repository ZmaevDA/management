package ru.zmaev.managment.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zmaev.managment.exception.ConflictException;
import ru.zmaev.managment.mapper.UserMapper;
import ru.zmaev.managment.model.dto.response.UserKeycloakDataResponse;
import ru.zmaev.managment.model.dto.response.UserResponse;
import ru.zmaev.managment.model.entity.User;
import ru.zmaev.managment.repository.UserRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public final String USER_EXISTS_BY_EMAIL = "USER_EXISTS_BY_EMAIL";

    @Transactional
    public UserResponse register(UserKeycloakDataResponse dto) {
        log.info("Creating new user with keycloak id: {}", dto.getKeycloakId());

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException(USER_EXISTS_BY_EMAIL, "Email already exists");
        }

        User user = new User();
        user.setKeycloakId(UUID.fromString(dto.getKeycloakId()));
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        User savedUser = userRepository.save(user);
        log.info("New user saved to database: {}", user);
        return userMapper.toResponse(savedUser);
    }
}
