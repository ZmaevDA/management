package ru.zmaev.managment.controller.openApi;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ru.zmaev.managment.model.dto.request.TaskCreateRequest;
import ru.zmaev.managment.model.dto.request.TaskUpdateRequest;
import ru.zmaev.managment.model.dto.response.TaskFilterRequest;
import ru.zmaev.managment.model.dto.response.TaskResponse;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;

import java.util.UUID;

public interface TaskOpenApi {
    ResponseEntity<Page<TaskResponse>> loadAll(TaskFilterRequest filter, int pageNumber, int pageSize);
    ResponseEntity<TaskResponse> loadById(UUID id);
    ResponseEntity<TaskResponse> create(TaskCreateRequest request);
    ResponseEntity<TaskResponse> changeStatus(UUID id, StatusType statusType);
    ResponseEntity<TaskResponse> changePriority(UUID id, PriorityType priorityType);
    ResponseEntity<TaskResponse> update(UUID id, TaskUpdateRequest request);
    ResponseEntity<Void> deleteById(UUID id);
    ResponseEntity<TaskResponse> assign(UUID taskId, UUID assignerId);
    ResponseEntity<TaskResponse> unassign(UUID taskId);
}
