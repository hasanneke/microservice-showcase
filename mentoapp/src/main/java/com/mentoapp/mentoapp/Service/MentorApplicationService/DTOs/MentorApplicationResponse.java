package com.mentoapp.mentoapp.Service.MentorApplicationService.DTOs;

import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.MentorApplication.ApplicationState;
import com.mentoapp.mentoapp.Entity.User.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentorApplicationResponse {
    private Long id;
    private User applicant;
    private Set<Category> categories;
    private String description;
    private ApplicationState state;
}
