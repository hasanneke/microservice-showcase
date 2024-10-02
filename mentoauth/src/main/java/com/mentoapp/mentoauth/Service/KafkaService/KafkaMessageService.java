package com.mentoapp.mentoauth.Service.KafkaService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());

    public void sendUserCreatedEvent(UserCreatedEvent userCreatedEvent) throws JsonProcessingException {
        UserCreatedEvent event = UserCreatedEvent.builder()
                .id(userCreatedEvent.getId())
                .email(userCreatedEvent.getEmail())
                .name(userCreatedEvent.getName()).build();

        String jsonString = om.writeValueAsString(event);
        Message<String> message = MessageBuilder
                .withPayload(jsonString)
                .setHeader(KafkaHeaders.TOPIC, "usercreated")
                .build();

        kafkaTemplate.send(message);
    }
}
