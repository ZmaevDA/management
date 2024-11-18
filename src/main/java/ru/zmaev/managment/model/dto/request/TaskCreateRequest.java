package ru.zmaev.managment.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zmaev.managment.model.enums.PriorityType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateRequest {
    private String title;
    private String description;
    private PriorityType priority;
}
