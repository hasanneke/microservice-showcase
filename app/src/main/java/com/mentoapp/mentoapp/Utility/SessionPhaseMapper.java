package com.mentoapp.mentoapp.Utility;

import com.mentoapp.mentoapp.Entity.Phase.MentorSessionPhase;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Service.MentorSessionService.DTOs.SessionResponse;
import com.mentoapp.mentoapp.Service.PhaseService.DTOs.SessionPhaseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface SessionPhaseMapper {
    SessionPhaseMapper INSTANCE = Mappers.getMapper(SessionPhaseMapper.class);

    SessionPhaseResponse phaseToPhaseResponse(MentorSessionPhase phase);
}
