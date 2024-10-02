package com.mentoapp.mail_service.Service.NotificationService;

import com.mentoapp.mail_service.Entity.Notification;
import com.mentoapp.mail_service.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public void saveNotification(Notification notification){
        notificationRepository.save(notification);
    }
}
