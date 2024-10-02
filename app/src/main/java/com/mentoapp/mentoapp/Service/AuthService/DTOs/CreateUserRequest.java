package com.mentoapp.mentoapp.Service.AuthService.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank
    @Email
    private String email;
    @Size(min = 8)
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    private String website;
    private String linkedIn;
}
