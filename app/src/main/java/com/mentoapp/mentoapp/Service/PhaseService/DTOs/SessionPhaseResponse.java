package com.mentoapp.mentoapp.Service.PhaseService.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mentoapp.mentoapp.Entity.Phase.PhaseState;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.SessionPhaseReview.SessionPhaseReview;
import com.mentoapp.mentoapp.Entity.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionPhaseResponse {
    private Long id;
    private PhaseState state;
    private User mentor;
    private User mentee;
    private MentorSession session;
    private Timestamp createdAt;
    private LocalDateTime endDate;
    private String title;
    private String description;
    private Set<SessionPhaseReview> reviews = new HashSet<>();
}
