package ru.zmaev.managment.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateRequest {
    private String title;
    private String description;
    private StatusType status;
    private PriorityType priority;
}
