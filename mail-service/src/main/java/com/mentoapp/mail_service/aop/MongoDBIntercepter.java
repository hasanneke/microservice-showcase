package com.mentoapp.mail_service.aop;

import com.mentoapp.mail_service.Entity.Notification;
import com.mentoapp.mail_service.Entity.NotificationType;
import com.mentoapp.mail_service.Service.NotificationService.NotificationService;
import com.mentoapp.mail_service.Util.Notifications.ApplicationApprovedNotification;
import com.mentoapp.mail_service.Util.Notifications.ApplicationRejectedNotification;
import com.mentoapp.mail_service.Util.Notifications.PhaseCreatedNotification;
import com.mentoapp.mail_service.Util.Notifications.WelcomeNotification;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MongoDBIntercepter {
    Logger logger = LoggerFactory.getLogger(MongoDBIntercepter.class);

    @Autowired
    private NotificationService notificationService;

    @AfterReturning(
            pointcut = "execution(* com.mentoapp.mail_service.Service.EmailService.EmailServiceListener.schedulePhaseEmail(..))",
            returning = "notification"
    )
    public void afterPhaseScheduledAdvisor(JoinPoint joinPoint, PhaseCreatedNotification notification) {
        logger.info(String.format("Saving PhaseReminder Notification %s", notification));
        notificationService.saveNotification(Notification
                .builder()
                .title(notification.getSubject())
                .content(notification.getBody())
                .type(NotificationType.phase_reminder)
                .build()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.mentoapp.mail_service.Service.EmailService.EmailServiceListener.sendWelcomeEmail(..))",
            returning = "notification"
    )
    public void afterPhaseScheduledMailAdvisor(JoinPoint joinPoint, WelcomeNotification notification) {
        logger.info(String.format("Saving Welcome Notification %s", notification));
        notificationService.saveNotification(Notification
                .builder()
                .title(notification.getSubject())
                .content(notification.getBody())
                .type(NotificationType.welcome)
                .build()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.mentoapp.mail_service.Service.EmailService.EmailServiceListener.sendEmailToMentor(..))",
            returning = "notification"
    )
    public void afterApplicationApprovedMailAdvisor(JoinPoint joinPoint, ApplicationApprovedNotification notification) {
        logger.info(String.format("Saving Application Approved Notification %s", notification));
        notificationService.saveNotification(Notification
                .builder()
                .title(notification.getSubject())
                .content(notification.getBody())
                .type(NotificationType.mentor_application_accepted)
                .build()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.mentoapp.mail_service.Service.EmailService.EmailServiceListener.sendApplicationRejectedMail(..))",
            returning = "notification"
    )
    public void afterApplicationApprovedMailAdvisor(JoinPoint joinPoint, ApplicationRejectedNotification notification) {
        logger.info(String.format("Saving Application Rejected Notification %s", notification));
        notificationService.saveNotification(Notification
                .builder()
                .title(notification.getSubject())
                .content(notification.getBody())
                .type(NotificationType.mentor_application_rejected)
                .build()
        );
    }
}
