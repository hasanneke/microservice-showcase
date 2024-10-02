package com.mentoapp.mail_service.Exception;

import jakarta.mail.MessagingException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public void handleGenericException(Exception ex) {
        logger.error("Internal Server Error: ", ex);
    }

    @ExceptionHandler(SchedulerException.class)
    public void handleSchedulerException(SchedulerException ex) {
        logger.error("Schedule Mail Failed. Exception: ", ex);
    }

    @ExceptionHandler(MessagingException.class)
    public void handleMessagingException(MessagingException ex) {
        logger.error("Mail Sending Failed. Exception: ", ex);
    }
}
