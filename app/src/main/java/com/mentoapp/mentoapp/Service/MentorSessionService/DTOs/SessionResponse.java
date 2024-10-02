package com.mentoapp.mentoapp.Service.MentorSessionService.DTOs;

import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.Phase.MentorSessionPhase;
import com.mentoapp.mentoapp.Entity.Session.SessionState;
import com.mentoapp.mentoapp.Entity.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionResponse {
    private long id;
    private User mentor;
    private User mentee;
    private SessionState state;
    private MentorSessionPhase activePhase;
    private Set<Category> categories;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Set<MentorSessionPhase> sessionPhases = new HashSet<>();
}
