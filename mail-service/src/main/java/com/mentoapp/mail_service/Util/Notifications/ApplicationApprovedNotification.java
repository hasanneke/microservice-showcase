package com.mentoapp.mail_service.Util.Notifications;

import lombok.Data;

@Data
public class ApplicationApprovedNotification extends CustomNotification {
    public ApplicationApprovedNotification() {
        this.subject = "Mentörlük Başvurun Kabul Edildi!";
        this.body = "Hemen menti aramaya basla!";
    }
}
