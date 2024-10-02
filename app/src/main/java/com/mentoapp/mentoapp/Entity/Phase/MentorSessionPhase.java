package com.mentoapp.mentoapp.Entity.Phase;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.SessionPhaseReview.SessionPhaseReview;
import com.mentoapp.mentoapp.Entity.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "session_phase")
public class MentorSessionPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "state")
    private PhaseState state;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "mentee_id")
    private User mentee;

    @ManyToOne()
    @JoinColumn(name = "session_id")
    @JsonBackReference
    private MentorSession session;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "phase")
    @JsonManagedReference
    private Set<SessionPhaseReview> reviews = new HashSet<>();
}
