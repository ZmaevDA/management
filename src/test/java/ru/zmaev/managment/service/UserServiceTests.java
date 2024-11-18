package ru.zmaev.managment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.zmaev.managment.auth.UserInfo;
import ru.zmaev.managment.exception.NotFoundException;
import ru.zmaev.managment.mapper.UserMapper;
import ru.zmaev.managment.model.dto.response.UserResponse;
import ru.zmaev.managment.model.entity.User;
import ru.zmaev.managment.repository.UserRepository;
import ru.zmaev.managment.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    private final UserRepository mockUserRepository = mock(UserRepository.class);
    private final UserMapper mockUserMapper = mock(UserMapper.class);
    private final UserInfo mockUserInfo = mock(UserInfo.class);

    private final UserService userService = new UserServiceImpl(mockUserRepository, mockUserMapper, mockUserInfo);

    private final UUID userId = UUID.randomUUID();
    private final String userEmail = "example@example.com";

    private final User user = new User();
    private final List<User> users = List.of(user);

    private final UserResponse userResponse = new UserResponse();

    @BeforeEach
    public void init() {
        user.setId(userId);
        user.setKeycloakId(userId);
        user.setEmail(userEmail);

        userResponse.setId(userId);
        userResponse.setKeycloakId(userId);
        userResponse.setEmail(userEmail);
    }

    @Test
    public void loadAll() {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> userPage = new PageImpl<>(users, pageable, 1);

        when(mockUserRepository.findAll(pageable)).thenReturn(userPage);
        when(mockUserMapper.toResponse(user)).thenReturn(userResponse);

        Page<UserResponse> result = userService.loadAll(0, 10);

        assertEquals(userResponse, result.getContent().get(0));

        verify(mockUserRepository, times(1)).findAll(pageable);
        verify(mockUserMapper, times(1)).toResponse(user);
    }

    @Test
    public void loadCurrentUserData() {
        when(mockUserInfo.getEmail()).thenReturn(userEmail);
        when(mockUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(mockUserMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.loadCurrentUserData();

        assertEquals(userResponse, result);
        verify(mockUserRepository, times(1)).findByEmail(userEmail);
        verify(mockUserMapper, times(1)).toResponse(user);
    }

    @Test
    public void loadCurrentUserData_notFound() {
        when(mockUserRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, userService::loadCurrentUserData);
    }

    @Test
    public void loadById() {
        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockUserMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.loadById(userId);

        assertEquals(userResponse, result);
        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockUserMapper, times(1)).toResponse(user);
    }

    @Test
    public void loadById_notFound() {
        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.loadById(userId));
    }
}
