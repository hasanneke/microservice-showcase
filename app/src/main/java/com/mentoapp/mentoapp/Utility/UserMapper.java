package com.mentoapp.mentoapp.Utility;
import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Service.UserService.DTOs.MentorResponse;
import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    UserResponse userToUserResponse(User user);

    @AfterMapping
    default void setFullName(@MappingTarget UserResponse userResponse, User user){
        userResponse.setFullName(user.getName() + " " + userResponse.getSurname());
    }

    @AfterMapping
    default void setFullName(@MappingTarget MentorResponse mentorResponse, User user){
        mentorResponse.setFullName(user.getName() + " " + mentorResponse.getSurname());
    }
}

