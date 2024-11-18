package ru.zmaev.managment.service;

import org.springframework.data.domain.Page;
import ru.zmaev.managment.model.dto.response.UserResponse;
import ru.zmaev.managment.model.entity.User;

import java.util.UUID;

public interface UserService {
    Page<UserResponse> loadAll(int pageNumber, int pageSize);

    UserResponse loadCurrentUserData();

    UserResponse loadById(UUID id);

    User loadUserByEmailOrThrow(String email);

    User loadUserByIdOrThrow(UUID id);
}
