package ru.zmaev.managment.mapper;

import org.mapstruct.Mapper;
import ru.zmaev.managment.model.dto.request.CommentRequest;
import ru.zmaev.managment.model.dto.response.CommentResponse;
import ru.zmaev.managment.model.entity.Comment;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {
    Comment toEntity(CommentRequest request);
    CommentResponse toResponse(Comment comment);
}
