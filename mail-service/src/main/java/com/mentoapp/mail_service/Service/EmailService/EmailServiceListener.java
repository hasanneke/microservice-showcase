package com.mentoapp.mail_service.Service.EmailService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mentoapp.mail_service.Entity.Notification;
import com.mentoapp.mail_service.Entity.NotificationType;
import com.mentoapp.mail_service.Service.EmailService.DTOs.*;
import com.mentoapp.mail_service.Service.NotificationService.NotificationService;
import com.mentoapp.mail_service.Util.Notifications.ApplicationApprovedNotification;
import com.mentoapp.mail_service.Util.Notifications.ApplicationRejectedNotification;
import com.mentoapp.mail_service.Util.Notifications.PhaseCreatedNotification;
import com.mentoapp.mail_service.Util.Notifications.WelcomeNotification;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EmailServiceListener {
    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    private ObjectMapper om;

    {
        new ObjectMapper().registerModule(new JavaTimeModule());
    }

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @KafkaListener(topics = "usercreated", groupId = "emailservice")
    public WelcomeNotification sendWelcomeEmail(String message) throws JsonProcessingException, MessagingException {
        UserCreated userCreated = om.readValue(message, UserCreated.class);

        WelcomeNotification notification = new WelcomeNotification();

        emailService.sendEmail(userCreated.getEmail(), notification.getSubject(), notification.getBody());

        notificationService.saveNotification(Notification.builder()
                .type(NotificationType.welcome)
                .receiverEmail(userCreated.getEmail())
                .title(notification.getSubject())
                .content(notification.getBody()).build());

        return notification;
    }

    @KafkaListener(topics = "mentorcreated", groupId = "emailservice")
    public ApplicationApprovedNotification sendEmailToMentor(String message) throws JsonProcessingException, MessagingException {
        MentorCreated mentorCreated = om.readValue(message, MentorCreated.class);

        ApplicationApprovedNotification notification = new ApplicationApprovedNotification();
        notification.setTo(mentorCreated.getEmail());
        emailService.sendEmail(mentorCreated.getEmail(),
                notification.getSubject(), notification.getBody()
        );
        return notification;
    }

    @KafkaListener(topics = "application_rejected", groupId = "emailservice")
    public ApplicationRejectedNotification sendApplicationRejectedMail(String message) throws JsonProcessingException, MessagingException {
        ApplicationRejected mentorCreated = om.readValue(message, ApplicationRejected.class);
        ;
        ApplicationRejectedNotification notification = new ApplicationRejectedNotification();
        notification.setTo(mentorCreated.getEmail());

        emailService.sendEmail(mentorCreated.getEmail(),
                notification.getSubject(), notification.getBody()
        );

        return notification;
    }

    @KafkaListener(topics = "phasecreated", groupId = "emailservice")
    public PhaseCreated schedulePhaseReminderEmail(String message) throws JsonProcessingException, SchedulerException {
        PhaseCreated event = om.readValue(message, PhaseCreated.class);
        schedulePhaseEmail(event.getMenteeEmail(), event.getEndDate());
        schedulePhaseEmail(event.getMentorEmail(), event.getEndDate());
        return event;
    }

    public PhaseCreatedNotification schedulePhaseEmail(String email, LocalDateTime dateTime) throws SchedulerException {
        PhaseCreatedNotification notification = new PhaseCreatedNotification();
        notification.setTo(email);

        emailService.scheduleEmail(ScheduleEmailRequest.builder()
                .email(email)
                .subject(notification.getSubject())
                .dateTime(dateTime)
                .body(notification.getBody())
                .build());

        return notification;
    }
}
