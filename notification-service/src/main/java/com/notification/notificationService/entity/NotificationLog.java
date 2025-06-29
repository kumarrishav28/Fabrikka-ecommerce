package com.notification.notificationService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification_log")
public class NotificationLog {

    @Id
    @Column(name =  "notif_log_id",unique = true,nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long notifLogId;

    @Column(name = "notif_temp_name")
    String notifTempName ;

    @Lob
    @Column(name = "exception_msg",columnDefinition = "TEXT")
    String logException;

    @Column(name = "exception_date")
    Date excpetionDate;

    @Column(name = "status")
    String status;

}
