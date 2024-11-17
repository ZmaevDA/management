package ru.zmaev.managment.model.dto.response;

import lombok.Data;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;

import java.time.Instant;
import java.util.UUID;

@Data
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private StatusType status;
    private PriorityType priority;
    private UserResponse author;
    private UserResponse assignee;
    private Instant createdAt;
    private Instant updatedAt;
}
