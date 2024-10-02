package com.mentoapp.mail_service.Service.EmailService.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PhaseCreated {
    private Long id;
    private LocalDateTime endDate;
    private String mentorEmail;
    private String menteeEmail;
}
