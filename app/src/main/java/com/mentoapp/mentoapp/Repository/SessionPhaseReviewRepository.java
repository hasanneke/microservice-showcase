package com.mentoapp.mentoapp.Repository;

import com.mentoapp.mentoapp.Entity.SessionPhaseReview.SessionPhaseReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SessionPhaseReviewRepository extends JpaRepository<SessionPhaseReview, Long> {
    List<SessionPhaseReview> findBySessionId(Long id);
    Set<SessionPhaseReview> findByPhaseId(Long id);
}
