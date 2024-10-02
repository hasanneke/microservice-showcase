package com.mentoapp.mentoapp.Controller.PhaseController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mentoapp.mentoapp.Entity.Phase.DTOs.CreateMentorSessionPhaseRequest;
import com.mentoapp.mentoapp.Entity.Phase.MentorSessionPhase;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Service.PhaseService.DTOs.SessionPhaseResponse;
import com.mentoapp.mentoapp.Service.PhaseService.PhaseService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
@RestController
@RequestMapping(path = "/api/v1/sessions")
@AllArgsConstructor
@RequiredArgsConstructor
public class PhaseController {
    @Autowired
    private PhaseService phaseService;

    @GetMapping("/{sessionId}/phases")
    ResponseEntity<List<MentorSessionPhase>> getPhases(@PathVariable(name = "sessionId") Long id){
        return ResponseEntity.ok(phaseService.getPhases(id));
    }

    @PostMapping(path = "/{sessionId}/phases")
    @PreAuthorize("hasAuthority('mentor')")
    ResponseEntity<MentorSessionPhase> createMentorSessionPhase(@PathVariable(name = "sessionId") Long sessionId, @RequestBody CreateMentorSessionPhaseRequest request) throws JsonProcessingException, AccessDeniedException {
        request.setSessionId(sessionId);
        phaseService.createPhase(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{sessionId}/phases/{id}")
    ResponseEntity<MentorSessionPhase> getPhase(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(phaseService.getPhase(id));
    }

    @GetMapping("/{sessionId}/phases/{id}/activate")
    @PreAuthorize("hasAuthority('mentor')")
    ResponseEntity activate(@PathVariable(name = "sessionId") Long sessionId, @PathVariable(name = "id") Long id){
        phaseService.activatePhase(sessionId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sessionId}/phases/{id}/complete")
    ResponseEntity<MentorSessionPhase> complete(@PathVariable(name = "sessionId") Long sessionId, @PathVariable(name = "id") Long id){
        return ResponseEntity.ok(phaseService.completePhase(sessionId, id));
    }

    @PostMapping("/{sessionId}/phases/{id}")
    @PreAuthorize("hasAuthority('mentor')")
    ResponseEntity<MentorSessionPhase> update(@PathVariable(name = "sessionId") Long sessionId,
                                              @PathVariable(name = "id") Long id,
                                              @Validated @RequestBody CreateMentorSessionPhaseRequest request) throws BadRequestException {
        return ResponseEntity.ok(phaseService.updatePhase(id,request));
    }

    @DeleteMapping("/{sessionId}/phases/{id}")
    @PreAuthorize("hasAuthority('mentor')")
    ResponseEntity<SessionPhaseResponse> delete( @PathVariable(name = "id") Long id) throws BadRequestException {
        return ResponseEntity.ok(phaseService.deletePhase(id));
    }
}
