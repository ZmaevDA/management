package ru.zmaev.managment.service;

import org.springframework.data.domain.Page;
import ru.zmaev.managment.model.dto.request.TaskCreateRequest;
import ru.zmaev.managment.model.dto.request.TaskUpdateRequest;
import ru.zmaev.managment.model.dto.response.TaskFilterRequest;
import ru.zmaev.managment.model.dto.response.TaskResponse;
import ru.zmaev.managment.model.entity.Task;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;

import java.util.UUID;

public interface TaskService {
    Page<TaskResponse> loadAll(TaskFilterRequest filter, int pageNumber, int pageSize);

    TaskResponse loadById(UUID id);

    TaskResponse create(TaskCreateRequest request);

    TaskResponse assign(UUID taskId, UUID assignerId);

    TaskResponse unassign(UUID taskId);

    TaskResponse changeStatus(UUID id, StatusType statusType);

    TaskResponse changePriority(UUID id, PriorityType priorityType);

    TaskResponse update(UUID id, TaskUpdateRequest request);

    void deleteById(UUID id);

    Task loadTaskByIdOrThrow(UUID id);
}
