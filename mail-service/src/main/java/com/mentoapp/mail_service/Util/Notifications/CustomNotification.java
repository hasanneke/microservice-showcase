package com.mentoapp.mail_service.Util.Notifications;

import com.mentoapp.mail_service.Entity.NotificationType;
import lombok.Data;

@Data
public abstract class CustomNotification {
    String to;
    String subject;
    String body;
}
