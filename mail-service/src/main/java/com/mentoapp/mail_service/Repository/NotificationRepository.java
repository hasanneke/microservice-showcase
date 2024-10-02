package com.mentoapp.mail_service.Repository;

import com.mentoapp.mail_service.Entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
