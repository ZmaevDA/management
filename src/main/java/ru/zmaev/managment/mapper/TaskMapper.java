package ru.zmaev.managment.mapper;

import org.mapstruct.Mapper;
import ru.zmaev.managment.model.dto.request.TaskCreateRequest;
import ru.zmaev.managment.model.dto.request.TaskUpdateRequest;
import ru.zmaev.managment.model.dto.response.TaskResponse;
import ru.zmaev.managment.model.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskResponse toResponse(Task task);
    Task toEntity(TaskCreateRequest request);
    Task toEntity(TaskUpdateRequest request);
}
