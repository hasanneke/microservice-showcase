package com.mentoapp.mentoapp.Service.KafkaService.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorCreatedEvent {
    private String email;
    private String name;
}
