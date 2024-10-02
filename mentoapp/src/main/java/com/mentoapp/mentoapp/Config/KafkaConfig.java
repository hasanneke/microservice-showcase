package com.mentoapp.mentoapp.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic mentorCreatedTopic(){
        return TopicBuilder.name("mentorcreated").build();
    }

    @Bean
    public NewTopic phaseCreatedTopic(){
        return TopicBuilder.name("phasecreated").build();
    }
}
