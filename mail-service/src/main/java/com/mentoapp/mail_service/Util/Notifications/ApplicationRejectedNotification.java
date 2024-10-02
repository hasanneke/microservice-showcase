package com.mentoapp.mail_service.Util.Notifications;

import lombok.Data;

@Data
public class ApplicationRejectedNotification extends CustomNotification {
    public ApplicationRejectedNotification() {
        this.subject = "Mentörlük Başvurun Reddedildi";
        this.body = "Gerekli koşulların sağlanmadığı gözlendi. Bundan dolayı başvuru reddedildi.";
    }
}
