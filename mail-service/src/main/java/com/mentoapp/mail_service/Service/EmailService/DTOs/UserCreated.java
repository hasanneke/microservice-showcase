package com.mentoapp.mail_service.Service.EmailService.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreated {
    private int id;
    private String email;
    private String name;
}

