package ru.zmaev.managment.model.dto.request;

import lombok.Data;
import ru.zmaev.managment.model.enums.PriorityType;

@Data
public class TaskCreateRequest {
    private String title;
    private String description;
    private PriorityType priority;
}
