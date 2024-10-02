package com.mentoapp.mentoapp.Entity.Session;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.Phase.MentorSessionPhase;
import com.mentoapp.mentoapp.Entity.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mentor_session")
public class MentorSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @ManyToOne
    @JoinColumn(name = "mentee_id")
    private User mentee;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "state")
    private SessionState state;

    @ManyToMany(mappedBy = "sessions")
    private Set<Category> categories;

    @CreationTimestamp
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "active_phase_id")
    private Long activePhaseId;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<MentorSessionPhase> sessionPhases = new HashSet<>();

    public void addPhase(MentorSessionPhase phase){
        sessionPhases.add(phase);
    }
}
