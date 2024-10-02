package com.mentoapp.mentoapp.Service.UserService.DTOs;

import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.User.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Relation(collectionRelation = "users")
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
