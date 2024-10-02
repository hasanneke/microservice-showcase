package com.mentoapp.mentoapp.Service.MentorSessionService.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.User.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMentorSessionRequest {
    @JsonIgnore
    private Long mentorId;
    private Set<Long> categories;
}
