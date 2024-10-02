package com.mentoapp.mail_service.Util.Notifications;

import lombok.Data;

@Data
public class WelcomeNotification extends CustomNotification {
    public WelcomeNotification() {
        this.subject = "Mento'ya Hoşgeldin!";
        this.body = "Hemen mentör aramaya başla!";
    }
}
