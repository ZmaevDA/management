package ru.zmaev.managment.model.dto.request;

import lombok.Data;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;

@Data
public class TaskUpdateRequest {
    private String title;
    private String description;
    private StatusType status;
    private PriorityType priority;
}
