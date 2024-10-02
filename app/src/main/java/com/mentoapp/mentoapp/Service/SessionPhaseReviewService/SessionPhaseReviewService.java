package com.mentoapp.mentoapp.Service.SessionPhaseReviewService;

import com.mentoapp.mentoapp.Entity.Phase.MentorSessionPhase;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.SessionPhaseReview.SessionPhaseReview;
import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Exception.Instance.ResourceNotFound;
import com.mentoapp.mentoapp.Exception.Instance.UserNotFoundException;
import com.mentoapp.mentoapp.Repository.MentorSessionPhaseRepository;
import com.mentoapp.mentoapp.Repository.MentorSessionRepository;
import com.mentoapp.mentoapp.Repository.SessionPhaseReviewRepository;
import com.mentoapp.mentoapp.Repository.UserRepository;
import com.mentoapp.mentoapp.Service.SessionPhaseReviewService.DTOs.SessionPhaseReviewRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class SessionPhaseReviewService {
    @Autowired
    private SessionPhaseReviewRepository sessionPhaseReviewRepository;

    @Autowired
    private MentorSessionPhaseRepository mentorSessionPhaseRepository;

    @Autowired
    private MentorSessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    // TODO: (*) A session can only have two review
    // TODO: (*) A session cannot be reviewed by same user again
    @Transactional
    public SessionPhaseReview reviewPhase(SessionPhaseReviewRequest request) {
        // TODO: check if phase is completed
        User reviewer =
                userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()-> new UserNotFoundException("User not found"));
        User reviewedUser = null;
        MentorSession session =
                sessionRepository.findById(request.getSessionId()).orElseThrow(() -> new ResourceNotFound("Session not found"));
        // TODO: (?) Improve this logic
        if (reviewer.getEmail().equals(session.getMentee().getEmail())) {
            reviewedUser =
                    userRepository.findByEmail(session.getMentor().getEmail()).orElseThrow(()-> new UserNotFoundException("User not found"));
        } else {
            reviewedUser =
                    userRepository.findByEmail(session.getMentee().getEmail()).orElseThrow(()-> new UserNotFoundException("User not found"));
        }

        int newReviewCount = reviewedUser.getReviewCount() + 1;
        double newReviewScore = (reviewedUser.getReviewScore() + request.getScore()) / newReviewCount;
        reviewedUser.setReviewCount(newReviewCount);
        reviewedUser.setReviewScore(newReviewScore);
        userRepository.save(reviewedUser);
        MentorSessionPhase mentorSessionPhase =
                mentorSessionPhaseRepository.findById(request.getPhaseId()).orElseThrow(() -> new ResourceNotFound("Phase not found"));

        return sessionPhaseReviewRepository.save(SessionPhaseReview.builder()
                .reviewer(reviewer)
                .reviewedUser(reviewedUser)
                .phase(mentorSessionPhase)
                .session(session)
                .description(request.getDescription())
                .score(request.getScore())
                .build()
        );
    }

    public List<SessionPhaseReview> getReviewsForSession(Long id){
        return sessionPhaseReviewRepository.findBySessionId(id);
    }
    public Set<SessionPhaseReview> getReviewsForPhase(Long id){
        return sessionPhaseReviewRepository.findByPhaseId(id);
    }
}
