package ru.zmaev.managment.mapper;

import org.mapstruct.Mapper;
import ru.zmaev.managment.model.dto.response.UserResponse;
import ru.zmaev.managment.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
