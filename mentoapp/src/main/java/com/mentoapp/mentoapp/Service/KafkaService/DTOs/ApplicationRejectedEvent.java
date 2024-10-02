package com.mentoapp.mentoapp.Service.KafkaService.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplicationRejectedEvent {
    private String email;
}
