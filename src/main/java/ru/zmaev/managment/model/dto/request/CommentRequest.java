package ru.zmaev.managment.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank(message = "Content must not be blank")
    @Size(max = 500, message = "Content must not exceed 500 characters")
    private String content;
}
