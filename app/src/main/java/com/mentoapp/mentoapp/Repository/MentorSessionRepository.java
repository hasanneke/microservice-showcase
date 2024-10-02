package com.mentoapp.mentoapp.Repository;

import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.Session.SessionState;
import com.mentoapp.mentoapp.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface MentorSessionRepository extends JpaRepository<MentorSession, Long> {
    List<MentorSession> findByMenteeId(long menteeId);
    @Query("SELECT s FROM MentorSession s WHERE s.mentee.id = :menteeId AND s.state IN:states")
    List<MentorSession> findByMenteeIdAndStates(long menteeId, Set<SessionState> states);
    List<MentorSession> findByMentorId(long mentorId);
    @Query("SELECT s FROM MentorSession s WHERE s.mentor.id = :mentorId AND s.state IN:states")
    List<MentorSession> findByMentorIdAndState(long mentorId, Set<SessionState> states);
    @Query("SELECT s FROM MentorSession s JOIN s.categories c WHERE c = :category AND s.mentor = :mentor AND s.state IN:states")
    List<MentorSession> findByMentorIdAndCategoryAndSessionState(User mentor, Category category, Set<SessionState> states);
    @Query("SELECT s FROM MentorSession s WHERE s.mentee = :mentee AND s.state IN:states")
    List<MentorSession> findByMenteeAndSessionStates(User mentee, Set<SessionState> states);
}
