package com.mentoapp.mail_service.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PublishedMessageAspect {
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Before("execution(* com.mentoapp.mail_service.Service.EmailService.EmailServiceListener.schedulePhaseReminderEmail(..)) && args(message,..)")
    public void phaseCreatedEventReceivedAdvice(JoinPoint joinPoint, String message) {
        logger.info(String.format("PhaseCreatedEvent received -> %s", message));
    }

    @Before("execution(* com.mentoapp.mail_service.Service.EmailService.EmailServiceListener.sendWelcomeEmail(..)) && args(message,..)")
    public void userCreatedEventReceivedAdvice(JoinPoint joinPoint, String message) {
        logger.info(String.format("User Created Event received -> %s", message));
    }

    @Before("execution(* com.mentoapp.mail_service.Service.EmailService.EmailServiceListener.sendEmailToMentor(..)) && args(message,..)")
    public void mentorCreatedEventReceivedAdvice(JoinPoint joinPoint, String message) {
        logger.info(String.format("MentorCreatedEvent received -> %s", message));
    }

    @Before("execution(* com.mentoapp.mail_service.Service.EmailService.EmailServiceListener.sendApplicationRejectedMail(..)) && args(message,..)")
    public void mentorApplicationRejectedEventReceivedAdvice(JoinPoint joinPoint, String message) {
        logger.info(String.format("ApplicationRejectedEvent received -> %s", message));
    }
}
