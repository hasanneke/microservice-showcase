package com.mentoapp.mentoapp.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mentoapp.mentoapp.Service.KafkaService.DTOs.ApplicationRejectedEvent;
import com.mentoapp.mentoapp.Service.MentorApplicationService.DTOs.MentorApplicationResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterThrowing(
            pointcut = "execution(* com.mentoapp.mentoapp.Service.MentorApplicationService.MentorApplicationService.createApplication(..))",
            throwing = "ex")
    public void afterThrowingApplicationCreateErrorAdvice(JoinPoint joinPoint, Throwable ex) throws JsonProcessingException {
        logger.error("Mentor Application Create failed. Exception: ", ex);
    }

    @AfterThrowing(
            pointcut = "execution(* com.mentoapp.mentoapp.Service.MentorApplicationService.MentorApplicationService.rejectApplication(..))",
            throwing = "ex")
    public void afterThrowingApplicationRejectedErrorAdvice(JoinPoint joinPoint, Throwable ex) throws JsonProcessingException {
        logger.error("Mentor Application Rejected failed. Exception: ", ex);
    }

    @AfterThrowing(
            pointcut = "execution(* com.mentoapp.mentoapp.Service.MentorApplicationService.MentorApplicationService.acceptApplication(..))",
            throwing = "ex")
    public void afterThrowingApplicationApprovedAdvice(JoinPoint joinPoint, Throwable ex) throws JsonProcessingException {
        logger.error("Mentor Application Approve failed. Exception: ", ex);
    }
}
