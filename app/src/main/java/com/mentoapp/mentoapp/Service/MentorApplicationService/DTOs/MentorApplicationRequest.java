package com.mentoapp.mentoapp.Service.MentorApplicationService.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class MentorApplicationRequest {
    @NotBlank
    private String description;
    @NotBlank
    private Set<Long> categoryIds;
}
