package ru.zmaev.managment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zmaev.managment.repository.UserRepository;
import ru.zmaev.managment.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
}
