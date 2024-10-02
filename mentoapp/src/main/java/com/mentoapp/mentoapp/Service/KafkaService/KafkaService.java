package com.mentoapp.mentoapp.Service.KafkaService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Service.KafkaService.DTOs.ApplicationRejectedEvent;
import com.mentoapp.mentoapp.Service.KafkaService.DTOs.PhaseCreatedEvent;

import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class KafkaService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());

    public void publishPhaseCreated(PhaseCreatedEvent phaseCreatedEvent) throws JsonProcessingException {
        String payload = om.writeValueAsString(phaseCreatedEvent);

        Message<String> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, "phasecreated")
                .build();

        kafkaTemplate.send(message);
    }

    public void publishMentorCreated(UserResponse userResponse) throws JsonProcessingException {
        String payload = om.writeValueAsString(userResponse);

        Message<String> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, "mentorcreated")
                .build();

        kafkaTemplate.send(message);
    }
    public void publishMentorApplicationRejected(ApplicationRejectedEvent applicationRejected) throws JsonProcessingException {
        String payload = om.writeValueAsString(applicationRejected);

        Message<String> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, "application_rejected")
                .build();
        kafkaTemplate.send(message);
    }
}
