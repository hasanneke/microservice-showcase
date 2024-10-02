package com.mentoapp.mentoapp.Utility;

import com.mentoapp.mentoapp.Entity.MentorApplication.MentorApplication;
import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Service.MentorApplicationService.DTOs.MentorApplicationResponse;
import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;

public class CustomMapper {
    public static UserResponse userToUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .roles(user.getRoles())
                .name(user.getName())
                .fullName(user.getFullName())
                .surname(user.getSurname())
                .categories(user.getCategories())
                .email(user.getEmail())
                .urlAvatar(user.getUrlAvatar())
                .reviewCount(user.getReviewCount())
                .sessionCount(user.getSessionCount())
                .reviewScore(user.getReviewScore())
                .linkedIn(user.getLinkedIn())
                .website(user.getWebsite())
                .description(user.getDescription())
                .build();
    }
    public static MentorApplicationResponse mentorApplicationToMentorApplicationResponse(MentorApplication mentorApplication){
        return MentorApplicationResponse.builder()
                .description(mentorApplication.getDescription())
                .id(mentorApplication.getId())
                .applicant(mentorApplication.getApplicant())
                .state(mentorApplication.getState())
                .categories(mentorApplication.getCategories())
                .description(mentorApplication.getDescription())
                .build();
    }
}
