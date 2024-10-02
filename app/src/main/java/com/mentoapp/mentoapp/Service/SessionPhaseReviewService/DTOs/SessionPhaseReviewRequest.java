package com.mentoapp.mentoapp.Service.SessionPhaseReviewService.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionPhaseReviewRequest {
    @JsonIgnore
    private Long sessionId;
    @JsonIgnore
    private Long phaseId;
    private String description;
    double score;
}
