package ru.zmaev.managment.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilterRequest {
    private String id;
    private String title;
    private String description;
    private StatusType status;
    private PriorityType priority;
    private UUID authorId;
    private UUID assignerId;
}
