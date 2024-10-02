package com.mentoapp.mentoapp.Service.MentorSessionService;

import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.Phase.MentorSessionPhase;
import com.mentoapp.mentoapp.Entity.Phase.PhaseState;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.Session.SessionState;
import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Exception.Instance.ResourceAlreadyUpdatedException;
import com.mentoapp.mentoapp.Exception.Instance.ResourceNotFound;
import com.mentoapp.mentoapp.Exception.Instance.UpdateFailedException;
import com.mentoapp.mentoapp.Exception.Instance.UserNotFoundException;
import com.mentoapp.mentoapp.Repository.CategoryRepository;
import com.mentoapp.mentoapp.Repository.MentorSessionPhaseRepository;
import com.mentoapp.mentoapp.Repository.MentorSessionRepository;
import com.mentoapp.mentoapp.Repository.UserRepository;
import com.mentoapp.mentoapp.Service.MentorSessionService.DTOs.CreateMentorSessionRequest;
import com.mentoapp.mentoapp.Service.MentorSessionService.DTOs.SessionResponse;
import com.mentoapp.mentoapp.Utility.SessionMapper;
import com.mentoapp.mentoapp.Entity.User.UserRole;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class MentorSessionService {
    @Autowired
    private MentorSessionRepository mentorSessionRepository;
    @Autowired
    private MentorSessionPhaseRepository mentorSessionPhaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // Get authenticated user's mentee sessions
    public List<SessionResponse> getSessionsMentee() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return mentorSessionRepository.findByMenteeId(user.get().getId())
                .stream().map((session)->{
                    SessionResponse sessionResponse = SessionMapper.INSTANCE.sessionToSessionResponse(session);
                    if(session.getActivePhaseId() != null){
                        sessionResponse.setActivePhase(
                                mentorSessionPhaseRepository.findById(session.getActivePhaseId()).orElse(null));
                    }
                    return sessionResponse;
                }).toList();
    }

    // Get authenticated user's mentee sessions
    public List<SessionResponse> getSessionsMentor() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return mentorSessionRepository.findByMentorId(user.get().getId())
                .stream().map((session)->{
                    SessionResponse sessionResponse = SessionMapper.INSTANCE.sessionToSessionResponse(session);
                    if(session.getActivePhaseId() != null){
                        sessionResponse.setActivePhase(
                                mentorSessionPhaseRepository.findById(session.getActivePhaseId()).orElse(null));
                    }
                        return sessionResponse;
                }).toList();
    }

    // Get authenticated user's mentee sessions by filter
    public List<SessionResponse> getSessionsMenteeByFilter(Set<SessionState> states) {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return mentorSessionRepository.findByMenteeIdAndStates(user.get().getId(), states)
                .stream().map((session)->{
                    SessionResponse sessionResponse = SessionMapper.INSTANCE.sessionToSessionResponse(session);
                    if(session.getActivePhaseId() != null){
                        sessionResponse.setActivePhase(
                                mentorSessionPhaseRepository.findById(session.getActivePhaseId()).orElse(null));
                    }
                    return sessionResponse;
                }).toList();
    }
    // Get authenticated user's mentor sessions by filter
    public List<SessionResponse> getSessionsMentorByFilter(Set<SessionState> states) {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return mentorSessionRepository.findByMentorIdAndState(user.get().getId(), states)
                .stream().map((session)->{
                    SessionResponse sessionResponse = SessionMapper.INSTANCE.sessionToSessionResponse(session);
                    if(session.getActivePhaseId() != null){
                        sessionResponse.setActivePhase(
                                mentorSessionPhaseRepository.findById(session.getActivePhaseId()).orElse(null));
                    }
                    return sessionResponse;
                }).toList();
    }

    @Transactional
    public MentorSession createSession(CreateMentorSessionRequest request) throws BadRequestException {
        // Get mentee
        User mentee =
                userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UserNotFoundException("User not found"));
        // Get mentor
        User mentor =
                userRepository.findById(request.getMentorId()).orElseThrow(() -> new ResourceNotFound("Mentor does not exist."));

        // Check if the user has mentor role
        if (!mentor.getRoles().contains(UserRole.mentor)){
            throw new BadRequestException("User is not a mentor");
        }

        // Check if user tries to create session with themselves
        if(mentor == mentee){
            throw new BadRequestException("User cannot create session with themselves");
        }

        // Get selected session categories
        List<Category> categories =
                categoryRepository.findAllById(request.getCategories());

        // Get parent category, if none return error
        Category category = categories.stream().filter((e)-> e.getLevel() == 0).findFirst().orElseThrow(()-> new ResourceNotFound("Category Not Found"));

        // Get mentor's sessions for waiting and started
        List<MentorSession> mentorsSessions = mentorSessionRepository.findByMentorIdAndCategoryAndSessionState(mentor, category, Set.of(SessionState.started, SessionState.waiting));

        // Get mentee's sessions for waiting and started
        List<MentorSession> menteeSessions = mentorSessionRepository.findByMenteeAndSessionStates(mentee, Set.of(SessionState.started, SessionState.waiting));


        // If mentee has two waiting or started sessions throw an error
        if (mentorsSessions.size() >= 2){
            throw new BadRequestException("Mentor already has two waiting or active session with given category");
        }
        // If mentor has two waiting or started sessions for the parent category, throw an error
        if(menteeSessions.size() >= 2){
            throw new BadRequestException("Mentee already has two waiting or active session");
        }

        // Update mentor and mentees user table for their session count
        updateUsersSessionCount(mentor, mentee);

        // Create session
        MentorSession mentorSession = mentorSessionRepository.save(MentorSession.builder()
                .mentor(mentor)
                .mentee(mentee)
                .state(SessionState.waiting)
                .build());

        for (Category element: categories) {
            element.addSession(mentorSession);
            categoryRepository.save(element);
        }

        return mentorSession;
    }

    public SessionResponse getSession(Long id) {
        MentorSession session = mentorSessionRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Session not found"));
        SessionResponse sessionResponse = SessionMapper.INSTANCE.sessionToSessionResponse(session);

        if (session.getActivePhaseId() != null){
            sessionResponse.setActivePhase(mentorSessionPhaseRepository.findById(session.getActivePhaseId()).orElse(null));
        }
        return sessionResponse;
    }


    @Transactional
    public MentorSession activateSession(Long id) throws UpdateFailedException {
        // Get session, or throw an error if session not exist
        MentorSession session = mentorSessionRepository.findById(id).orElseThrow(()->new ResourceNotFound("Session not found"));

        // Get session phases
        List<MentorSessionPhase> phases = mentorSessionPhaseRepository.findBySessionIdAndStateOrderByEndDateAsc(id, PhaseState.waiting);

        // Check if session has any phases, otherwise, throw an error
        if (phases.isEmpty()){
            throw new UpdateFailedException("Session does not have any phases");
        }
        // Update session as started
        session.setState(SessionState.started);

        // Retrieve the most recent waiting phase to activate
        MentorSessionPhase mostRecentPhase = mentorSessionPhaseRepository.findBySessionIdAndStateOrderByEndDateAsc(id, PhaseState.waiting).stream().findFirst().orElse(null);

        // If there is no phases to start throw an error
        if (mostRecentPhase == null){
             throw new ResourceAlreadyUpdatedException("Session doesn't have more waiting phases");
        }else{
            // Start phase
            mostRecentPhase.setState(PhaseState.started);
            // Set activePhaseId field of session
            session.setActivePhaseId(mostRecentPhase.getId());
        }
        return session;
    }

    private void updateUsersSessionCount(User mentor, User mentee) {
        mentor.setSessionCount(mentor.getSessionCount() + 1);
        mentee.setSessionCount(mentee.getSessionCount() + 1);
        userRepository.saveAll(Set.of(mentor, mentee));
    }
}
