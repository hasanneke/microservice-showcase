package com.mentoapp.mail_service.Service.EmailService;

import com.mentoapp.mail_service.Service.EmailService.DTOs.EmailJob;
import com.mentoapp.mail_service.Service.EmailService.DTOs.ScheduleEmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Scheduler scheduler;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
        messageHelper.setSubject(subject);
        messageHelper.setText(body, true);
        messageHelper.setFrom("hasaneke1000@gmail.com");
        messageHelper.setTo(toEmail);

        mailSender.send(message);


    }

    public void scheduleEmail(ScheduleEmailRequest scheduleEmailRequest) throws SchedulerException {
        ZonedDateTime dateTime = ZonedDateTime.of(scheduleEmailRequest.getDateTime(), ZoneId.systemDefault());
        JobDetail jobDetail = buildJobDetail(scheduleEmailRequest);
        Trigger trigger = buildJobTrigger(jobDetail, dateTime);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private JobDetail buildJobDetail(ScheduleEmailRequest scheduleEmailRequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", scheduleEmailRequest.getEmail());
        jobDataMap.put("subject", scheduleEmailRequest.getSubject());
        jobDataMap.put("body", scheduleEmailRequest.getBody());

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Email Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}