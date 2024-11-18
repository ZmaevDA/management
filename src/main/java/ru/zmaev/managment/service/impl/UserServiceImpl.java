package ru.zmaev.managment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zmaev.managment.auth.UserInfo;
import ru.zmaev.managment.exception.NotFoundException;
import ru.zmaev.managment.mapper.UserMapper;
import ru.zmaev.managment.model.dto.response.UserResponse;
import ru.zmaev.managment.model.entity.User;
import ru.zmaev.managment.repository.UserRepository;
import ru.zmaev.managment.service.UserService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserInfo userInfo;

    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";

    @Override
    public Page<UserResponse> loadAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
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
    public User loadUserByIdOrThrow(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND, "User with id " + id + " not found")
        );
    }
}
