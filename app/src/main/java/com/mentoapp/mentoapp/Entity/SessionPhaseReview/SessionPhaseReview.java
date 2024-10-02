package com.mentoapp.mentoapp.Entity.SessionPhaseReview;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mentoapp.mentoapp.Entity.Phase.MentorSessionPhase;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.N;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SessionPhaseReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne()
    @JoinColumn(name = "phase_id")
    @JsonBackReference
    private MentorSessionPhase phase;

    @ManyToOne()
    @JoinColumn(name = "session_id")
    @JsonBackReference
    private MentorSession session;

    @ManyToOne()
    @JoinColumn(name = "reviewed_user_id")
    @JsonBackReference
    private User reviewedUser;

    @Column(name = "description")
    private String description;

    @Max(5)
    @Min(1)
    @Column(name = "score")
    private Double score;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_At")
    private Timestamp updatedAt;
}
