package ru.zmaev.managment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zmaev.managment.auth.UserInfo;
import ru.zmaev.managment.exception.NotFoundException;
import ru.zmaev.managment.mapper.UserMapper;
import ru.zmaev.managment.model.dto.response.UserResponse;
import ru.zmaev.managment.model.entity.User;
import ru.zmaev.managment.repository.UserRepository;
import ru.zmaev.managment.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserInfo userInfo;

    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    public UserResponse loadCurrentUserData() {
        log.info("Loading current user data");
        User user = loadUserByEmailOrThrow(userInfo.getEmail());
        return userMapper.toResponse(user);
    }

    @Override
    public User loadUserByEmailOrThrow(String email) {
       return userRepository.findByEmail(email).orElseThrow(
               () -> new NotFoundException(USER_NOT_FOUND, "User with email " + email + " not found")
       );
    }

    @Override
    public User loadUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND, "User with id " + id + " not found")
        );
    }
}
