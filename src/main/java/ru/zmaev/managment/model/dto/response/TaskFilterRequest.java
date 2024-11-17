package ru.zmaev.managment.model.dto.response;

import lombok.Data;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;

import java.util.UUID;

@Data
public class TaskFilterRequest {
    private String id = null;
    private String title  = null;
    private String description  = null;
    private StatusType status  = null;
    private PriorityType priority  = null;
    private UUID authorId  = null;
    private UUID assigneeId  = null;
}
