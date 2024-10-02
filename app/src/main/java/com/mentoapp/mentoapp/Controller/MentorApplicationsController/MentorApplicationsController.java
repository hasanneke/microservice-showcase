package com.mentoapp.mentoapp.Controller.MentorApplicationsController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mentoapp.mentoapp.Entity.MentorApplication.MentorApplication;
import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Service.MentorApplicationService.DTOs.MentorApplicationRequest;
import com.mentoapp.mentoapp.Service.MentorApplicationService.DTOs.MentorApplicationResponse;

import com.mentoapp.mentoapp.Service.MentorApplicationService.MentorApplicationService;
import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("api/v1/applications")
@AllArgsConstructor
@RequiredArgsConstructor
public class MentorApplicationsController {
    @Autowired
    private MentorApplicationService mentorApplicationService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<EntityModel<MentorApplicationResponse>> getMentorApplication(@PathVariable Long id) throws NoSuchMethodException {
        // Get application
        MentorApplicationResponse mentorApplicationResponse = mentorApplicationService.getApplication(id);
        // Create entity model
        EntityModel<MentorApplicationResponse> mentorApplicationResponseEntityModel = EntityModel.of(mentorApplicationResponse);
        // Generate and add link to itself
        Method method = this.getClass().getMethod("getMentorApplication", Long.class);
        mentorApplicationResponseEntityModel.add(linkTo(method, id).withSelfRel());

        return new HttpEntity<>(mentorApplicationResponseEntityModel);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin')")
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<PagedModel<MentorApplicationResponse>> getMentorApplications(@RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size) throws NoSuchMethodException {
        // Retrieve mentor applications paginated
        Page<MentorApplication> mentorApplicationPage = mentorApplicationService.getApplications(PageRequest.of(page, size));

        // Map entities to DTOs (MentorApplication -> MentorApplicationResponse)
        List<MentorApplicationResponse> applications = mentorApplicationPage
                .getContent().stream().map((e) -> MentorApplicationResponse.builder()
                        .applicant(e.getApplicant())
                        .description(e.getDescription())
                        .state(e.getState())
                        .categories(e.getCategories())
                        .id(e.getId()).build()
                )
                .toList();
        // Create PagedModel to insert links and necessary headers
        PagedModel<MentorApplicationResponse> pagedModel = PagedModel.of(
                        applications,
                        new PagedModel.PageMetadata(mentorApplicationPage.getSize(),
                        mentorApplicationPage.getNumber(),
                        mentorApplicationPage.getTotalElements(),
                        mentorApplicationPage.getTotalPages()));

        // Method for previous and next links
        Method method = this.getClass().getMethod("getMentorApplications", Integer.class, Integer.class);

        if (page < mentorApplicationPage.getTotalPages()) {
            Link link = linkTo(method, page + 1, size).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }
        if (page > 0) {
            Link link = linkTo(method, page - 1, size).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return new HttpEntity<>(pagedModel);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MentorApplicationResponse> createApplication(@RequestBody MentorApplicationRequest request) throws NoSuchMethodException {
        // Create mentor application
        MentorApplicationResponse mentorApplicationResponse = mentorApplicationService.createApplication(request);
        // Generate link to itself and attach to 'Location' header
        Method method = this.getClass().getMethod("getMentorApplication", Long.class);
        return ResponseEntity.created(linkTo(method,
                mentorApplicationResponse.getId()).toUri()).body(mentorApplicationResponse);
    }

    @GetMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('admin')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MentorApplication> acceptApplication(@PathVariable Long id) throws JsonProcessingException {
        MentorApplication response =
                mentorApplicationService.acceptApplication(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/deny")
    @PreAuthorize("hasAuthority('admin')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MentorApplicationResponse> rejectApplication(@PathVariable Long id) throws JsonProcessingException {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        mentorApplicationService.rejectApplication(id);
        return ResponseEntity.noContent().build();
    }
}
