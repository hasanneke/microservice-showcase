package com.mentoapp.mentoapp.Utility;

import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Service.MentorSessionService.DTOs.SessionResponse;
import com.mentoapp.mentoapp.Service.UserService.DTOs.MentorResponse;
import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface SessionMapper {
    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);
    @Mapping(target = "activePhase", ignore = true)
    SessionResponse sessionToSessionResponse(MentorSession user);
}
