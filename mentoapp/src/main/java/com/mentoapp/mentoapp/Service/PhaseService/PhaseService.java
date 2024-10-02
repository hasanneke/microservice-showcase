package com.mentoapp.mentoapp.Service.PhaseService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mentoapp.mentoapp.Service.KafkaService.DTOs.PhaseCreatedEvent;
import com.mentoapp.mentoapp.Entity.Phase.DTOs.CreateMentorSessionPhaseRequest;
import com.mentoapp.mentoapp.Entity.Phase.MentorSessionPhase;
import com.mentoapp.mentoapp.Entity.Phase.PhaseState;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.Session.SessionState;
import com.mentoapp.mentoapp.Exception.Instance.ResourceNotFound;
import com.mentoapp.mentoapp.Repository.MentorSessionPhaseRepository;
import com.mentoapp.mentoapp.Repository.MentorSessionRepository;
import com.mentoapp.mentoapp.Repository.SessionPhaseReviewRepository;
import com.mentoapp.mentoapp.Service.KafkaService.KafkaService;
import com.mentoapp.mentoapp.Service.PhaseService.DTOs.SessionPhaseResponse;
import com.mentoapp.mentoapp.Utility.SessionPhaseMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class PhaseService {
    private static final Logger logger = LoggerFactory.getLogger(PhaseService.class);
    @Autowired
    private MentorSessionPhaseRepository mentorSessionPhaseRepository;
    @Autowired
    private MentorSessionRepository mentorSessionRepository;
    @Autowired
    private SessionPhaseReviewRepository sessionPhaseReviewRepository;
    @Autowired
    private KafkaService kafkaService;

    public MentorSessionPhase getPhase(Long phaseId) {
        MentorSessionPhase phase = mentorSessionPhaseRepository.findById(phaseId).orElseThrow(()->new ResourceNotFound("Phase not found"));
        phase.setReviews(sessionPhaseReviewRepository.findByPhaseId(phaseId));
        return phase;
    }

    public List<MentorSessionPhase> getPhases(Long sessionId) {
        List<MentorSessionPhase> phases = mentorSessionPhaseRepository.findBySessionId(sessionId);
        for (MentorSessionPhase phase:phases
             ) {
            phase.setReviews(sessionPhaseReviewRepository.findByPhaseId(phase.getId()));
        }
        return phases;
    }

    @Transactional
    public MentorSessionPhase createPhase(CreateMentorSessionPhaseRequest mentorSessionPhaseRequest) throws JsonProcessingException, AccessDeniedException {
        logger.info(String.format("Create phase request received -> %s", mentorSessionPhaseRequest));
        MentorSession mentorSession =
                mentorSessionRepository.findById(mentorSessionPhaseRequest.getSessionId()).orElseThrow(() -> new ResourceNotFound("Session not found"));

        if(!mentorSession.getMentor().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            throw new AccessDeniedException("Not a mentor");
        }

       return mentorSessionPhaseRepository.save(MentorSessionPhase.builder()
                .session(mentorSession)
                .mentor(mentorSession.getMentor())
                .mentee(mentorSession.getMentee())
                .state(PhaseState.waiting)
                .title(mentorSessionPhaseRequest.getTitle())
                .description(mentorSessionPhaseRequest.getDescription())
                .endDate(mentorSessionPhaseRequest.getEndDate())
                .build());

    }

    @Transactional
    public MentorSessionPhase activatePhase(Long sessionId, Long phaseId){
        MentorSession mentorSession =
                mentorSessionRepository.findById(sessionId).orElseThrow(() -> new ResourceNotFound("Session not found"));
        MentorSessionPhase mentorSessionPhase =
                mentorSessionPhaseRepository.findById(phaseId).orElseThrow(()-> new ResourceNotFound("Phase not found"));
        mentorSessionPhase.setState(PhaseState.started);
        mentorSession.setActivePhaseId(mentorSessionPhase.getId());
        mentorSessionRepository.save(mentorSession);
        return mentorSessionPhaseRepository.save(mentorSessionPhase);
    }

    @Transactional
    public MentorSessionPhase completePhase(Long sessionId, Long phaseId){
        MentorSession mentorSession =
                mentorSessionRepository.findById(sessionId).orElseThrow(() -> new ResourceNotFound("Session not found"));
        MentorSessionPhase mentorSessionPhase =
                mentorSessionPhaseRepository.findById(phaseId).orElseThrow(()-> new ResourceNotFound("Phase not found"));
        List<MentorSessionPhase> phases = mentorSessionPhaseRepository.findBySessionIdAndState(sessionId, PhaseState.waiting);
        if (phases.isEmpty()){
            mentorSessionPhase.setState(PhaseState.completed);
            mentorSession.setActivePhaseId(null);
            mentorSession.setState(SessionState.completed);
        }else {
            MentorSessionPhase mostRecentPhase = mentorSessionPhaseRepository.findBySessionIdAndStateOrderByEndDateAsc(sessionId, PhaseState.waiting)
                    .stream().filter((e)-> !e.getId().equals(mentorSessionPhase.getId())).findFirst().orElse(null);
            mentorSessionPhase.setState(PhaseState.completed);
            mostRecentPhase.setState(PhaseState.started);
            mentorSessionPhaseRepository.save(mostRecentPhase);
            mentorSession.setActivePhaseId(mostRecentPhase.getId());
            mentorSessionRepository.save(mentorSession);
        }
        return mentorSessionPhaseRepository.save(mentorSessionPhase);
    }

    @Transactional
    public MentorSessionPhase updatePhase(Long phaseId, CreateMentorSessionPhaseRequest createMentorSessionPhaseRequest) throws BadRequestException {
        MentorSessionPhase mentorSessionPhase =
                mentorSessionPhaseRepository.findById(phaseId).orElseThrow(()-> new ResourceNotFound("Phase not found"));
        MentorSession mentorSession =  mentorSessionPhase.getSession();
        if(mentorSession.getState() == SessionState.waiting){
            mentorSessionPhase.setTitle(createMentorSessionPhaseRequest.getTitle());
            mentorSessionPhase.setDescription(createMentorSessionPhaseRequest.getDescription());
            mentorSessionPhase.setEndDate(createMentorSessionPhaseRequest.getEndDate());
            return mentorSessionPhaseRepository.save(mentorSessionPhase);
        }else{
            throw new BadRequestException("Session started or completed");
        }
    }

    @Transactional
    public SessionPhaseResponse deletePhase(Long phaseId) throws BadRequestException {
      MentorSessionPhase mentorSessionPhase =  mentorSessionPhaseRepository.findById(phaseId).orElseThrow(()-> new ResourceNotFound("Phase not found"));
      MentorSession mentorSession =  mentorSessionPhase.getSession();
      if(mentorSession.getState() == SessionState.waiting){
          mentorSessionPhaseRepository.delete(mentorSessionPhase);
      }else{
          throw new BadRequestException("Session started or completed");
      }
      return SessionPhaseMapper.INSTANCE.phaseToPhaseResponse(mentorSessionPhase);
    }
}
