package ru.zmaev.managment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.zmaev.managment.auth.UserInfo;
import ru.zmaev.managment.exception.ConflictException;
import ru.zmaev.managment.exception.ForbiddenException;
import ru.zmaev.managment.exception.NotFoundException;
import ru.zmaev.managment.mapper.TaskMapper;
import ru.zmaev.managment.model.dto.request.TaskCreateRequest;
import ru.zmaev.managment.model.dto.request.TaskUpdateRequest;
import ru.zmaev.managment.model.dto.response.TaskFilterRequest;
import ru.zmaev.managment.model.dto.response.TaskResponse;
import ru.zmaev.managment.model.entity.Task;
import ru.zmaev.managment.model.entity.User;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;
import ru.zmaev.managment.repository.TaskRepository;
import ru.zmaev.managment.service.impl.TaskServiceImpl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TaskServiceTests {

    private final UserService mockUserService = mock(UserService.class);
    private final TaskRepository mockTaskRepository = mock(TaskRepository.class);
    private final TaskMapper mockTaskMapper = mock(TaskMapper.class);

    private final UserInfo mockUserInfo = mock(UserInfo.class);

    private final TaskService taskService = new TaskServiceImpl(
            mockUserService,
            mockTaskRepository,
            mockTaskMapper,
            mockUserInfo
    );

    private final static String taskTitle = "Title";
    private final static String userEmail = "test@test.ru";

    private final UUID taskId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();

    private final TaskCreateRequest taskCreateRequest = new TaskCreateRequest();
    private final TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest();
    private final Task task = new Task();
    private final TaskFilterRequest filter = new TaskFilterRequest();
    private final List<Task> tasks = new ArrayList<>();
    private final TaskResponse taskResponse = new TaskResponse();
    private final User user = new User();

    @BeforeEach
    public void init() {
        taskCreateRequest.setTitle(taskTitle);
        taskUpdateRequest.setTitle(taskTitle);

        filter.setId(taskId.toString());

        task.setId(taskId);
        task.setTitle(taskTitle);

        taskResponse.setId(taskId);
        taskResponse.setTitle(taskTitle);

        user.setId(userId);
        user.setEmail(userEmail);

        mockUserInfo.setEmail(userEmail);

        tasks.add(task);
    }

    @Test
    public void loadAll() {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Task> taskPage = new PageImpl<>(tasks, pageable, 1);
        when(mockTaskRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(taskPage);
        when(mockTaskMapper.toResponse(task)).thenReturn(taskResponse);

        Page<TaskResponse> result = taskService.loadAll(filter, pageNumber, pageSize);

        assertEquals(1, result.getTotalElements());
        assertEquals(taskResponse, result.getContent().get(0));
        verify(mockTaskRepository, times(1)).findAll(any(Specification.class), eq(pageable));
        verify(mockTaskMapper, times(1)).toResponse(task);
    }

    @Test
    public void loadById() {
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockTaskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.loadById(taskId);

        assertEquals(taskResponse, result);
        verify(mockTaskRepository, times(1)).findById(taskId);
        verify(mockTaskMapper, times(1)).toResponse(task);
    }

    @Test
    public void loadById_notFound() {
        UUID unrepresentedTaskId = UUID.randomUUID();
        when(mockTaskRepository.findById(unrepresentedTaskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.loadById(unrepresentedTaskId));
        verify(mockTaskRepository, times(1)).findById(unrepresentedTaskId);
    }

    @Test
    public void create() {
        when(mockTaskMapper.toEntity(taskCreateRequest)).thenReturn(task);
        when(mockUserInfo.getEmail()).thenReturn(userEmail);
        when(mockUserService.loadUserByEmailOrThrow(userEmail)).thenReturn(user);
        when(mockTaskRepository.save(any(Task.class))).thenReturn(task);
        when(mockTaskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.create(taskCreateRequest);

        assertEquals(taskResponse, result);
        verify(mockTaskRepository, times(1)).save(any(Task.class));
        verify(mockUserService, times(1)).loadUserByEmailOrThrow(userEmail);
    }

    @Test
    public void assign() {
        task.setAuthor(user);
        user.setKeycloakId(userId);
        when(mockUserInfo.getUserId()).thenReturn(userId);
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockUserInfo.getEmail()).thenReturn(userEmail);
        when(mockUserService.loadUserById(userId)).thenReturn(user);
        when(mockTaskRepository.save(any(Task.class))).thenReturn(task);
        when(mockTaskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.assign(taskId, userId);

        assertEquals(taskResponse, result);
        verify(mockTaskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void assign_taskNotFound() {
        UUID unrepresentedTaskId = UUID.randomUUID();
        when(mockTaskRepository.findById(unrepresentedTaskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.assign(unrepresentedTaskId, userId));
        verify(mockTaskRepository, times(1)).findById(unrepresentedTaskId);
    }

    @Test
    public void assign_forbidden() {
        task.setAuthor(user);
        when(mockUserInfo.getUserId()).thenReturn(userId);
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockUserInfo.getEmail()).thenReturn(userEmail);
        when(mockUserService.loadUserById(userId)).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> taskService.assign(taskId, userId));
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void unassign() {
        task.setAssignee(user);
        task.setAuthor(user);
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockTaskRepository.save(any(Task.class))).thenReturn(task);
        when(mockTaskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);

        TaskResponse result = taskService.unassign(taskId);

        assertEquals(taskResponse, result);
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void unassign_taskNotFound() {
        UUID unrepresentedTaskId = UUID.randomUUID();
        when(mockTaskRepository.findById(unrepresentedTaskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.unassign(unrepresentedTaskId));
        verify(mockTaskRepository, times(1)).findById(unrepresentedTaskId);
    }

    @Test
    public void unassign_taskNotTaken() {
        task.setAuthor(user);
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));

        assertThrows(ConflictException.class, () -> taskService.unassign(taskId));
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void unassign_forbidden() {
        User assigner = new User();
        assigner.setId(UUID.randomUUID());

        task.setAuthor(user);
        task.setAssignee(assigner);

        when(mockUserInfo.getUserId()).thenReturn(userId);
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockUserService.loadUserById(userId)).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> taskService.assign(taskId, userId));
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void changeStatus() {
        task.setAuthor(user);
        task.setAssignee(user);
        user.setKeycloakId(userId);

        when(mockUserInfo.getUserId()).thenReturn(userId);
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockUserService.loadUserById(userId)).thenReturn(user);
        when(mockTaskRepository.save(any(Task.class))).thenReturn(task);
        when(mockTaskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.changeStatus(taskId, StatusType.BLOCKED);

        assertEquals(taskResponse, result);
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void changeStatus_taskNotFound() {
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.changeStatus(taskId, StatusType.BLOCKED));
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void changeStatus_forbidden() {
        task.setAuthor(user);
        task.setAssignee(user);

        when(mockUserInfo.getUserId()).thenReturn(userId);
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockUserService.loadUserById(userId)).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> taskService.changePriority(taskId, PriorityType.HIGH));
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void changePriority() {
        task.setAuthor(user);
        task.setAssignee(user);
        user.setKeycloakId(userId);

        when(mockUserInfo.getUserId()).thenReturn(userId);
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockUserService.loadUserById(userId)).thenReturn(user);
        when(mockTaskRepository.save(any(Task.class))).thenReturn(task);
        when(mockTaskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.changePriority(taskId, PriorityType.HIGH);

        assertEquals(taskResponse, result);
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void changePriority_taskNotFound() {
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.changePriority(taskId, PriorityType.HIGH));
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void changePriority_forbidden() {
        task.setAuthor(user);
        task.setAssignee(user);

        when(mockUserInfo.getUserId()).thenReturn(userId);
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockUserService.loadUserById(userId)).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> taskService.changeStatus(taskId, StatusType.BLOCKED));
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void update() {
        task.setAuthor(user);
        task.setAssignee(user);

        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockTaskMapper.toEntity(any(TaskUpdateRequest.class))).thenReturn(task);
        when(mockUserService.loadUserById(userId)).thenReturn(user);
        when(mockTaskRepository.save(any(Task.class))).thenReturn(task);
        when(mockTaskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.update(taskId, taskUpdateRequest);

        assertEquals(taskResponse, result);
        verify(mockTaskRepository, times(1)).findById(taskId);
        verify(mockTaskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void update_forbidden() {
        task.setAuthor(user);
        task.setAssignee(user);
        user.setKeycloakId(userId);

        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockTaskMapper.toEntity(any(TaskUpdateRequest.class))).thenReturn(task);
        when(mockUserService.loadUserById(userId)).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> taskService.update(taskId, taskUpdateRequest));
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void updateNotFound() {
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.update(taskId, taskUpdateRequest));
        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void deleteById() {
        task.setAuthor(user);
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteById(taskId);
        verify(mockTaskRepository, times(1)).findById(taskId);
        verify(mockTaskRepository, times(1)).delete(task);
    }

    @Test
    public void deleteById_taskNotFound() {
        task.setAuthor(user);
        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.deleteById(taskId));

        verify(mockTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void deleteById_forbidden() {
        User assigner = new User();
        assigner.setId(UUID.randomUUID());
        assigner.setKeycloakId(UUID.randomUUID());
        task.setAuthor(user);
        task.setAssignee(assigner);
        user.setKeycloakId(UUID.randomUUID());

        when(mockUserInfo.getUserId()).thenReturn(userId);

        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));

        assertThrows(ForbiddenException.class, () -> taskService.deleteById(taskId));
        verify(mockTaskRepository, times(1)).findById(taskId);
    }
}
