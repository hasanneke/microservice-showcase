package com.mentoapp.mentoapp.Service.MentorApplicationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mentoapp.mentoapp.Repository.CategoryRepository;
import com.mentoapp.mentoapp.Service.KafkaService.DTOs.ApplicationRejectedEvent;
import com.mentoapp.mentoapp.Service.KafkaService.KafkaService;
import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.MentorApplication.ApplicationState;
import com.mentoapp.mentoapp.Entity.MentorApplication.MentorApplication;
import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Exception.Instance.ResourceNotFound;
import com.mentoapp.mentoapp.Exception.Instance.ResourceAlreadyUpdatedException;
import com.mentoapp.mentoapp.Exception.Instance.ResourceAlreadyExistException;
import com.mentoapp.mentoapp.Exception.Instance.UserNotFoundException;
import com.mentoapp.mentoapp.Repository.MentorApplicationRepository;
import com.mentoapp.mentoapp.Repository.UserRepository;
import com.mentoapp.mentoapp.Service.MentorApplicationService.DTOs.MentorApplicationRequest;
import com.mentoapp.mentoapp.Service.MentorApplicationService.DTOs.MentorApplicationResponse;
import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;
import com.mentoapp.mentoapp.Utility.CustomMapper;
import com.mentoapp.mentoapp.Entity.User.UserRole;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

// TODO: Use Mapper for MentorApplicationResponse from MentorApplication
@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class MentorApplicationService {
    @Autowired
    private MentorApplicationRepository mentorApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private KafkaService kafkaService;
    // TODO: implement mentees can only view their own application
    public MentorApplicationResponse getApplication(Long id) {
        MentorApplication mentorApplication = mentorApplicationRepository.findById(id).orElseThrow(() -> new ResourceNotFound("application not found"));
        return CustomMapper.mentorApplicationToMentorApplicationResponse(mentorApplication);
    }

    public Page<MentorApplication> getApplications(Pageable pageable) {
        return mentorApplicationRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // TODO: Check if categories have at least one category level 0
    @Transactional
    public MentorApplicationResponse createApplication(@RequestBody MentorApplicationRequest mentorApplicationRequest) {
        // Retrieve applicant
        User applicant = getApplicant();
        // Check if there is application already
        checkIfApplicationAlreadyMade(applicant);

        // Create application
        MentorApplication mentorApplication = mentorApplicationRepository.save(MentorApplication.builder()
                .state(ApplicationState.waiting)
                .description(mentorApplicationRequest.getDescription())
                .applicant(applicant)
                .build());

        // Set<Category> categories = mentorApplicationRepository.findByIds(mentorApplicationRequest.getCategoryIds());
        List<Category> categories = categoryRepository.findAllById(mentorApplicationRequest.getCategoryIds());
        for (Category category:categories
             ) {
            category.addApplication(mentorApplication);
            categoryRepository.save(category);
        }

        return CustomMapper.mentorApplicationToMentorApplicationResponse(mentorApplication);
    }

    @Transactional
    public MentorApplication acceptApplication(Long id) throws JsonProcessingException {
        MentorApplication mentorApplication = mentorApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Application not found"));

        if (mentorApplication.getState() != ApplicationState.waiting) {
            throw new ResourceAlreadyUpdatedException("application is already updated");
        }

        mentorApplication.setState(ApplicationState.accepted);
        MentorApplication savedMentorApplication = mentorApplicationRepository.save(mentorApplication);

        User user = updateUserRoleAndCategories(mentorApplication);

        // Publish Message to kafka mentor created
        kafkaService.publishMentorCreated(CustomMapper.userToUserResponse(user));

        return savedMentorApplication;
    }


    @Transactional
    public MentorApplicationResponse rejectApplication(Long id) {
        MentorApplication mentorApplication = getApplicationWithChecks(id);

        mentorApplication.setState(ApplicationState.rejected);
        MentorApplication savedMentorApplication = mentorApplicationRepository.save(mentorApplication);

        return CustomMapper.mentorApplicationToMentorApplicationResponse(savedMentorApplication);
    }

    private MentorApplication getApplicationWithChecks(Long id) {
        MentorApplication mentorApplication = mentorApplicationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Application not found"));
        if (mentorApplication.getState() != ApplicationState.waiting) {
            throw new ResourceAlreadyUpdatedException("application is already updated");
        }
        return mentorApplication;
    }


    private User updateUserRoleAndCategories(MentorApplication mentorApplication) {
        // Update user roles and categories
        User user =
                userRepository.findById(mentorApplication.getApplicant().getId()).orElseThrow(UserNotFoundException::new);
        user.setDescription(mentorApplication.getDescription());
        user.addRole(UserRole.mentor);
        User savedUser = userRepository.save(user);
        List<Category> categories = categoryRepository.findAllById(mentorApplication.getCategories().stream().map(Category::getId).toList());
        for (Category category: categories
        ) {
            category.addUser(savedUser);
            categoryRepository.save(category);
        }
        savedUser.setCategories(Set.copyOf(categories));
        return savedUser;
    }

    private User getApplicant() {
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private void checkIfApplicationAlreadyMade(User applicant) {
        List<MentorApplication> checkMentorApplication = mentorApplicationRepository.findByApplicant(applicant).orElse(null);

        if (checkMentorApplication != null && !checkMentorApplication.isEmpty()) {
            throw new ResourceAlreadyExistException("Application with id: " + checkMentorApplication.getFirst().getId() + " is already made");
        }
    }
}
