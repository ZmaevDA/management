package ru.zmaev.managment.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    @NotBlank(message = "Content must not be blank")
    @Size(max = 500, message = "Content must not exceed 500 characters")
    private String content;
}
