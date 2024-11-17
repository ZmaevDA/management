package ru.zmaev.managment.service;

import ru.zmaev.managment.model.entity.User;

import java.util.UUID;

public interface UserService {
    User loadUserByEmailOrThrow(String email);
    User loadUserById(UUID id);
}
