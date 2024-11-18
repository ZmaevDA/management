package ru.zmaev.managment.model.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class TaskWithCommentsResponse {
    TaskResponse task;
    Page<CommentResponse> comments;
}
