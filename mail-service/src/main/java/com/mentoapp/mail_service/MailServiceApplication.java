package com.mentoapp.mail_service;

import com.mentoapp.mail_service.Entity.Notification;
import com.mentoapp.mail_service.Entity.NotificationType;
import com.mentoapp.mail_service.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class MailServiceApplication implements CommandLineRunner {

    @Autowired
    private NotificationRepository notificationRepository;

	public static void main(String[] args) {
		SpringApplication.run(MailServiceApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        notificationRepository.save(Notification.builder()
                .receiverEmail("dasdsdsa@gmail.com")
                .type(NotificationType.welcome)
                .content("sadsadas")
                .title("asdsadsadsa")
                .build()
        );
    }
}
