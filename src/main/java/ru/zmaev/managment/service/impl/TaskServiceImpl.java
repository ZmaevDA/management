package ru.zmaev.managment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zmaev.managment.auth.UserInfo;
import ru.zmaev.managment.exception.ConflictException;
import ru.zmaev.managment.exception.ForbiddenException;
import ru.zmaev.managment.exception.NotFoundException;
import ru.zmaev.managment.mapper.CommentMapper;
import ru.zmaev.managment.mapper.TaskMapper;
import ru.zmaev.managment.model.dto.request.TaskCreateRequest;
import ru.zmaev.managment.model.dto.request.TaskUpdateRequest;
import ru.zmaev.managment.model.dto.response.TaskFilterRequest;
import ru.zmaev.managment.model.dto.response.TaskResponse;
import ru.zmaev.managment.model.entity.Task;
import ru.zmaev.managment.model.entity.User;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.RoleType;
import ru.zmaev.managment.model.enums.StatusType;
import ru.zmaev.managment.repository.CommentRepository;
import ru.zmaev.managment.repository.TaskRepository;
import ru.zmaev.managment.repository.specification.TaskSpecification;
import ru.zmaev.managment.service.TaskService;
import ru.zmaev.managment.service.UserService;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    public static final String TASK_NOT_FOUND = "TASK_NOT_FOUND";
    public static final String TASK_NO_ACCESS = "TASK_NO_ACCESS";
    public static final String TASK_TAKEN = "TASK_TAKEN";
    public static final String TASK_NOT_TAKEN = "TASK_NOT_TAKEN";

    private final UserService userService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserInfo userInfo;

    @Override
    public Page<TaskResponse> loadAll(TaskFilterRequest filter, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Specification<Task> specification = TaskSpecification.filterTasks(filter);
        Page<Task> tasks = taskRepository.findAll(specification, pageable);
        return tasks.map(taskMapper::toResponse);
    }

    @Override
    public TaskResponse loadById(UUID id) {
        Task task = loadTaskByIdOrThrow(id);
        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse create(TaskCreateRequest request) {
        Task task = taskMapper.toEntity(request);
        User user = userService.loadUserByEmailOrThrow(userInfo.getEmail());
        task.setStatus(StatusType.CREATED);
        task.setCreatedAt(Instant.now());
        task.setAuthor(user);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse assign(UUID taskId, UUID assigneeId) {
        Task task = loadTaskByIdOrThrow(taskId);
        User user = userService.loadUserByIdOrThrow(assigneeId);
        if (!userInfo.getUserId().equals(user.getKeycloakId()) &&
                !userInfo.getRole().contains(RoleType.ROLE_ADMIN.name()) &&
                !userInfo.getUserId().equals(task.getAuthor().getKeycloakId())) {
            throw new ForbiddenException(TASK_NO_ACCESS, "You can`t assign another person if you are not the administrator or author");
        }
        if (task.getAssignee() != null && !userInfo.getRole().contains(RoleType.ROLE_ADMIN.name())) {
            throw new ConflictException(TASK_TAKEN, "This task is already taken");
        }
        task.setAssignee(user);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse unassign(UUID taskId) {
        Task task = loadTaskByIdOrThrow(taskId);
        if (task.getAssignee() == null) {
            throw new ConflictException(TASK_NOT_TAKEN, "This task is not taken");
        }
        checkAuthorAssignerAdminAccessOrThrow(task);
        task.setAssignee(null);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse changeStatus(UUID id, StatusType statusType) {
        Task task = loadTaskByIdOrThrow(id);
        checkAuthorAssignerAdminAccessOrThrow(task);
        task.setStatus(statusType);
        task.setUpdatedAt(Instant.now());
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse changePriority(UUID id, PriorityType priorityType) {
        Task task = loadTaskByIdOrThrow(id);
        checkAuthorAssignerAdminAccessOrThrow(task);
        task.setPriority(priorityType);
        task.setUpdatedAt(Instant.now());
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse update(UUID id, TaskUpdateRequest request) {
        Task presentedTask = loadTaskByIdOrThrow(id);
        checkAuthorAssignerAdminAccessOrThrow(presentedTask);
        Task task = taskMapper.toEntity(request);
        task.setId(id);
        task.setAuthor(presentedTask.getAuthor());
        task.setAssignee(presentedTask.getAssignee());
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        Task task = loadTaskByIdOrThrow(id);
        checkAuthorAssignerAdminAccessOrThrow(task);
        taskRepository.delete(task);
    }

    public Task loadTaskByIdOrThrow(UUID id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new NotFoundException(TASK_NOT_FOUND, "Task with id " + id + " not found"));
    }

    private void checkAuthorAssignerAdminAccessOrThrow(Task task) {
        if (!Objects.equals(userInfo.getUserId(), task.getAuthor().getKeycloakId()) &&
                !userInfo.getRole().contains(RoleType.ROLE_ADMIN.name()) &&
                !Objects.equals(userInfo.getUserId(), task.getAssignee().getKeycloakId())
        ) {
            throw new ForbiddenException(TASK_NO_ACCESS, "You are not the author or assigner or do not have admin privileges.");
        }
    }
}
