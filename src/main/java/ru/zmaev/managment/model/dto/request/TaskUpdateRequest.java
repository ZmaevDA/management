package ru.zmaev.managment.model.dto.request;

import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;

public class TaskUpdateRequest {
    private String title;
    private String description;
    private StatusType status;
    private PriorityType priority;
}
