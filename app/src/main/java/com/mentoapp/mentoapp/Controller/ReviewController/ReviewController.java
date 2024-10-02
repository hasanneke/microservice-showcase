package com.mentoapp.mentoapp.Controller.ReviewController;

import com.mentoapp.mentoapp.Entity.SessionPhaseReview.SessionPhaseReview;
import com.mentoapp.mentoapp.Service.SessionPhaseReviewService.DTOs.SessionPhaseReviewRequest;
import com.mentoapp.mentoapp.Service.SessionPhaseReviewService.SessionPhaseReviewService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequiredArgsConstructor
public class ReviewController {
    @Autowired
    private SessionPhaseReviewService sessionPhaseReviewService;

    @PostMapping(path = "/api/v1/sessions/{sessionId}/phases/{phaseId}/review")
    public SessionPhaseReview reviewPhase(
            @PathVariable(name = "sessionId") Long sessionId,
            @PathVariable(name = "phaseId") Long phaseId,
            @RequestBody SessionPhaseReviewRequest request) {
        return sessionPhaseReviewService.reviewPhase(SessionPhaseReviewRequest.builder().
                sessionId(sessionId).
                phaseId(phaseId).
                score(request.getScore()).
                description(request.getDescription()).
                build());
    }

    @GetMapping(path = "/api/v1/sessions/{sessionId}/reviews")
    public List<SessionPhaseReview> getReviewsForSession(@PathVariable(name = "sessionId") Long sessionId) {
        return sessionPhaseReviewService.getReviewsForSession(sessionId);
    }

    @GetMapping(path = "/api/v1/sessions/{sessionId}/phases/{phaseId}/reviews")
    public Set<SessionPhaseReview> getReviewsForPhase(@PathVariable(name = "sessionId") Long sessionId, @PathVariable(name = "phaseId") Long phaseId) {
        return sessionPhaseReviewService.getReviewsForPhase(phaseId);
    }
}
