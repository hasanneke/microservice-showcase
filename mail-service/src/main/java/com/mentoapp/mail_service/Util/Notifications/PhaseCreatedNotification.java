package com.mentoapp.mail_service.Util.Notifications;

import lombok.Data;

@Data
public class PhaseCreatedNotification extends CustomNotification {
    public PhaseCreatedNotification() {
        this.subject = "Fazın bitmesine bir saat!";
        this.body = "Faz bitmeden fazı tamamlayıp değerlendirin";
    }
}
