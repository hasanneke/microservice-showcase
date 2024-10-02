package com.mentoapp.mentoauth.Service.AuthService.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class RegisterResponse {
    private String token;
}
