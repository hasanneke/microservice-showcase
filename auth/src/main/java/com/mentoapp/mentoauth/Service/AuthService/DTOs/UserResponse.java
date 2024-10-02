package com.mentoapp.mentoauth.Service.AuthService.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private long id;
    private String email;
    private String name;
    private String surname;
    private String fullName;
    private String urlAvatar;
    private Set<UserRole> roles;
    private String description;
}

