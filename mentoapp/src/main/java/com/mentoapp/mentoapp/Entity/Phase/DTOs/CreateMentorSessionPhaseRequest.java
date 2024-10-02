package com.mentoapp.mentoapp.Entity.Phase.DTOs;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class CreateMentorSessionPhaseRequest {
    @JsonIgnore
    private Long sessionId;
    private LocalDateTime endDate;
    private String title;
    private String description;
}
