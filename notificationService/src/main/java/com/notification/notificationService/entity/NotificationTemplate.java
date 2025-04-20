package com.notification.notificationService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification_template")
public class NotificationTemplate {

    @Id
    @Column(name = "notif_temp_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long notifTempId ;

    @Column(name = "template_name",nullable = false)
    String templateName;

    @Column (name =  "subject",nullable = false)
    String subject;

    @Column (name = "dynamic_fields")
    String dynamicFields;
}
