package com.mentoapp.mentoapp.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mentoapp.mentoapp.Entity.MentorApplication.MentorApplication;
import com.mentoapp.mentoapp.Entity.Phase.MentorSessionPhase;
import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Repository.UserRepository;
import com.mentoapp.mentoapp.Service.KafkaService.DTOs.ApplicationRejectedEvent;
import com.mentoapp.mentoapp.Service.KafkaService.DTOs.MentorCreatedEvent;
import com.mentoapp.mentoapp.Service.KafkaService.DTOs.PhaseCreatedEvent;
import com.mentoapp.mentoapp.Service.KafkaService.KafkaService;
import com.mentoapp.mentoapp.Service.MentorApplicationService.DTOs.MentorApplicationResponse;
import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;
import com.mentoapp.mentoapp.Utility.CustomMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class PublishingMessageAspect {
    final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private UserRepository userRepository;

    @AfterReturning(
            pointcut = "execution(* com.mentoapp.mentoapp.Service.PhaseService.PhaseService.createPhase(..))",
            returning = "phase")
    public void afterPhaseCreatedAdvice(JoinPoint joinPoint, MentorSessionPhase phase) throws JsonProcessingException {
            PhaseCreatedEvent event = PhaseCreatedEvent.builder()
                    .id(phase.getId())
                    .mentorEmail(phase.getMentor().getEmail())
                    .endDate(phase.getEndDate())
                    .menteeEmail(phase.getMentee().getEmail())
                    .build();
            logger.info(String.format("PhaseCreatedEvent Request Received -> %s", event));
            kafkaService.publishPhaseCreated(event);
            logger.info(String.format("PhaseCreatedEvent sent to Kafka -> %s", event));
    }

    @AfterReturning(
            pointcut = "execution(* com.mentoapp.mentoapp.Service.MentorApplicationService.MentorApplicationService.rejectApplication(..))",
            returning = "application")
    public void afterApplicationRejectedAdvice(JoinPoint joinPoint, MentorApplicationResponse application) throws JsonProcessingException {
        ApplicationRejectedEvent event = ApplicationRejectedEvent.builder()
                .email(application.getApplicant().getEmail())
                .build();
        logger.info(String.format("ApplicationRejectedEvent Request Received -> %s", event));
        kafkaService.publishMentorApplicationRejected(event);
        logger.info(String.format("ApplicationRejectedEvent sent to Kafka -> %s", event));
    }

//    @AfterReturning(
//            pointcut = "execution(* com.mentoapp.mentoapp.Service.MentorApplicationService.MentorApplicationService.acceptApplication(..))",
//            returning = "application")
//    public void afterApplicationApprovedAdvice(JoinPoint joinPoint, MentorApplication application) throws JsonProcessingException {
//        Optional<User> user = userRepository.findByEmail(application.getApplicant().getEmail());
//        UserResponse mentor = CustomMapper.userToUserResponse(user.get());
//        kafkaService.publishMentorCreated(mentor);
//        logger.info(String.format("MentorCreatedEvent sent to Kafka -> %s", mentor));
//    }
}
