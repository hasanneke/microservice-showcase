package com.mentoapp.mentoapp.Controller.SessionController;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.Session.SessionState;
import com.mentoapp.mentoapp.Exception.Instance.UpdateFailedException;
import com.mentoapp.mentoapp.Service.MentorSessionService.DTOs.CreateMentorSessionRequest;
import com.mentoapp.mentoapp.Service.MentorSessionService.DTOs.SessionResponse;
import com.mentoapp.mentoapp.Service.MentorSessionService.MentorSessionService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/sessions")
@AllArgsConstructor
@RequiredArgsConstructor
public class SessionController {
    @Autowired
    private MentorSessionService mentorSessionService;

    @GetMapping("/me/mentee")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<SessionResponse>> getSessionsMentee(@RequestParam(name = "state", required = false) Set<SessionState> states){
        List<SessionResponse> response;
        if (states != null && !states.isEmpty()){
            response = mentorSessionService.getSessionsMenteeByFilter(states);
        }else{
            response = mentorSessionService.getSessionsMentee();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/mentor")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<SessionResponse>> getSessionsMentor(@RequestParam(name = "state", required = false) Set<SessionState> states){
        List<SessionResponse> response;
        if (states != null && !states.isEmpty()){
            response = mentorSessionService.getSessionsMentorByFilter(states);
        }else{
            response = mentorSessionService.getSessionsMentor();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/mentor/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<MentorSession> createSession(@PathVariable(name = "id") Long mentorId, @RequestBody CreateMentorSessionRequest request) throws BadRequestException {
        request.setMentorId(mentorId);
        MentorSession response =  mentorSessionService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<SessionResponse> getSession(@PathVariable(name = "sessionId") Long sessionId){
        SessionResponse response =  mentorSessionService.getSession(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{sessionId}/activate")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('mentor')")
    ResponseEntity<MentorSession> activateSession(@PathVariable(name = "sessionId") Long sessionId) throws UpdateFailedException {
        MentorSession response =  mentorSessionService.activateSession(sessionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
