package com.mentoapp.mail_service.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document("notifications")
public class Notification {
    @Id
    private String id;
    private String title;
    private String content;
    private NotificationType type;
    private String receiverEmail;
}
