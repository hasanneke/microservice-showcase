package com.mentoapp.mentoapp.Service.SessionPhaseReviewService.DTOs;

import com.mentoapp.mentoapp.Entity.Phase.MentorSessionPhase;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.User.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionPhaseReviewResponse {
    private Long id;
    private User reviewer;
    private MentorSessionPhase phase;
    private MentorSession session;
    private User reviewedUser;
    private String description;
    private Double score;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean isReviewed;
}
