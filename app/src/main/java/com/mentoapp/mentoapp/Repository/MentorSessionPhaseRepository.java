package com.mentoapp.mentoapp.Repository;

import com.mentoapp.mentoapp.Entity.Phase.MentorSessionPhase;
import com.mentoapp.mentoapp.Entity.Phase.PhaseState;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.Session.SessionState;
import com.mentoapp.mentoapp.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface MentorSessionPhaseRepository extends JpaRepository<MentorSessionPhase, Long> {

    List<MentorSessionPhase> findBySessionId(Long id);
    List<MentorSessionPhase> findBySessionIdAndStateOrderByEndDateAsc(Long sessionId, PhaseState state);
    List<MentorSessionPhase> findBySessionIdAndState(Long id, PhaseState state);
    @Query("SELECT p FROM MentorSessionPhase p WHERE p.session.id = :sessionId AND p.state IN:states AND p.id != :id")
    List<MentorSessionPhase> findBySessionIdAndStatesAndNotId(Long sessionId, Set<PhaseState> states, Long id);
}
