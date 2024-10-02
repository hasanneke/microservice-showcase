package com.mentoapp.mentoapp.Service.UserService.DTOs;

import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.User.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorResponse {
    private long id;
    private String email;
    private String name;
    private String surname;
    private String fullName;
    private String urlAvatar;
    private Set<Category> categories;
    private Set<UserRole> roles;
    private double review;
}
