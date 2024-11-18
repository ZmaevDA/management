package ru.zmaev.managment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.zmaev.managment.controller.openApi.TaskOpenApi;
import ru.zmaev.managment.model.dto.request.TaskCreateRequest;
import ru.zmaev.managment.model.dto.request.TaskUpdateRequest;
import ru.zmaev.managment.model.dto.response.TaskFilterRequest;
import ru.zmaev.managment.model.dto.response.TaskResponse;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;
import ru.zmaev.managment.service.impl.TaskServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
public class TaskController implements TaskOpenApi {
    private final TaskServiceImpl taskService;

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<TaskResponse>> loadAll(@ModelAttribute TaskFilterRequest filter,
                                                      @RequestParam(defaultValue = "0") int pageNumber,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(taskService.loadAll(filter, pageNumber, pageSize));
    }

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskResponse> loadById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.loadById(id));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskResponse> create(@RequestBody TaskCreateRequest request) {
        return ResponseEntity.ok(taskService.create(request));
    }

    @Override
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskResponse> changeStatus(@PathVariable UUID id, @RequestParam StatusType statusType) {
        return ResponseEntity.ok(taskService.changeStatus(id, statusType));
    }

    @Override
    @PatchMapping("/{id}/priority")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskResponse> changePriority(@PathVariable UUID id, @RequestParam PriorityType priorityType) {
        return ResponseEntity.ok(taskService.changePriority(id, priorityType));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskResponse> update(@PathVariable UUID id, @RequestBody TaskUpdateRequest request) {
        return ResponseEntity.ok(taskService.update(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{taskId}/assigners/{assignerId}")
    public ResponseEntity<TaskResponse> assign(@PathVariable UUID taskId, @PathVariable UUID assignerId) {
        return ResponseEntity.ok(taskService.assign(taskId, assignerId));
    }

    @Override
    @PatchMapping("/{taskId}/unassigned")
    public ResponseEntity<TaskResponse> unassign(@PathVariable UUID taskId) {
        return ResponseEntity.ok(taskService.unassign(taskId));
    }
}
