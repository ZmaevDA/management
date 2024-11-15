package ru.zmaev.managment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zmaev.managment.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

}
