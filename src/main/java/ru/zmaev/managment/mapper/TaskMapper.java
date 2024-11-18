package ru.zmaev.managment.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.zmaev.managment.model.dto.request.TaskCreateRequest;
import ru.zmaev.managment.model.dto.request.TaskUpdateRequest;
import ru.zmaev.managment.model.dto.response.CommentResponse;
import ru.zmaev.managment.model.dto.response.TaskResponse;
import ru.zmaev.managment.model.dto.response.TaskWithCommentsResponse;
import ru.zmaev.managment.model.entity.Comment;
import ru.zmaev.managment.model.entity.Task;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface TaskMapper {
    TaskResponse toResponse(Task task);

    Task toEntity(TaskCreateRequest request);

    Task toEntity(TaskUpdateRequest request);

    default TaskWithCommentsResponse toResponseWithComments(Task task, Page<Comment> commentsPage, CommentMapper commentMapper) {
        TaskResponse taskResponse = toResponse(task);

        Page<CommentResponse> commentResponses = commentsPage.map(commentMapper::toResponse);

        TaskWithCommentsResponse response = new TaskWithCommentsResponse();
        response.setTask(taskResponse);
        response.setComments(commentResponses);

        return response;
    }

}
