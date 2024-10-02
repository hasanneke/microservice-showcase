package com.mentoapp.searchservice.Domain;

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
    private Set<Category> categories;
    private Set<UserRole> roles;
    private String description;
    private Integer sessionCount;
    private Integer reviewCount;
    private double reviewScore;
    private String linkedIn;
    private String website;
}

