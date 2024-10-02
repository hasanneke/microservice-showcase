package com.mentoapp.mentoauth.Service.AuthService.DTOs;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TokenResponse {
    private String token;
    private Date expireDate;
    private TokenResponse refreshToken;
}
